package se.kth.tablespoon.agent.main;

import java.io.IOException;
import java.util.Properties;

import com.aphyr.riemann.client.RiemannClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.agent.handlers.Configuration;
import se.kth.tablespoon.agent.events.EventSender;

import se.kth.tablespoon.agent.listeners.CollectlListener;
import se.kth.tablespoon.agent.listeners.ConfigListener;
import se.kth.tablespoon.agent.util.Sleep;

public class Start {
  
  private static final int TRIES = 1000;
  private static final int RECONNECTION_TIME = 5000;
  private static final int WAIT_FOR_QUEUE_TO_FILL_TIME = 1000;
  private static final int WAIT_FOR_THREAD_TO_DIE_TIME = 500;
  private static final int READ_QUEUE_TIME = 10; //10
  private static final String DEFAULT_COLLECTl_CONFIG = "collectl -P -o U";
  private static String host;
  private static int port;
  private static RiemannClient riemannClient;
  private static CollectlListener collectlListener;
  private static ConfigListener configListener;
  private static Configuration cph;
  private final static Logger slf4jLogger = LoggerFactory.getLogger(Start.class);
  
  static Thread t;
  
  public static void main(String[] args) {
    cph = new Configuration();
    try {
      cph.reload();
    } catch (Exception e) {
      slf4jLogger.error("Configuration could not be loaded. Exiting agent");
      System.exit(0);
    }
    Properties prop = cph.getProp();
    host = prop.getProperty("riemann.host");
    port = Integer.parseInt(prop.getProperty("riemann.port"));
     
    setUpHook();
    collectlListener = new CollectlListener();
    collectlListener.setCollectlConfig(DEFAULT_COLLECTl_CONFIG);
//		ConfigMessageHandler cmh = new ConfigMessageHandler(collectlListener);
//		configListener = new ConfigListener(4444, cmh);
    t = new Thread(collectlListener);
    t.start();
    while(collectlListener.queueIsEmpty()) {
      slf4jLogger.info("waiting...");
      Sleep.now(WAIT_FOR_QUEUE_TO_FILL_TIME);
    }
    connect(0);
    System.exit(0); //will trigger the exit handler which kills the thread
  }
  
  private static void connect(int tries)  {
    EventSender es;
    try {
      riemannClient = RiemannClient.tcp(host, port);
      riemannClient.connect();
      slf4jLogger.info("Established connection with host:" + host + " port:" + port);
      tries = 0; //resetting number of tries
      es = new EventSender(collectlListener.getRowQueue(), collectlListener.getEventLayouts(), riemannClient);
      while (true) {
        // it apparently needs some headroom.
        Sleep.now(READ_QUEUE_TIME);
        if (!collectlListener.queueIsEmpty()) es.sendMetrics();
      }
    } catch (IOException e) {
      riemannClient.close();
      slf4jLogger.info("Connection with server could not be established.");
      if (tries < TRIES) {
        slf4jLogger.info("Waiting for " + Math.round(RECONNECTION_TIME/1000) + " seconds and attempting to connect again...");
        Sleep.now(RECONNECTION_TIME);
        connect(++tries);
      }
    }
  }
  
  
  private static void setUpHook() {
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
      public void run() {
        if (collectlListener != null) {
          collectlListener.requestInterrupt();
          while(t.isAlive()) {
            slf4jLogger.info("waiting for thread to die...");
            Sleep.now(WAIT_FOR_THREAD_TO_DIE_TIME);
          }
        }
        if (configListener != null) {
          configListener.requestInterrupt();
          while(t.isAlive()) {
            slf4jLogger.info("waiting for thread to die...");
            Sleep.now(WAIT_FOR_THREAD_TO_DIE_TIME);
          }
        }
        if (riemannClient != null) {
          if (riemannClient.isConnected()) {
            riemannClient.close();
            slf4jLogger.info("Closed the connection with the Riemann client.");
          }
        }
      }
    }));
  }
  
}
