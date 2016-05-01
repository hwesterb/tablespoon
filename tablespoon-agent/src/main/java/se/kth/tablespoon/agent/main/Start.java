package se.kth.tablespoon.agent.main;

import java.io.IOException;

import com.aphyr.riemann.client.RiemannClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.agent.general.Configuration;
import se.kth.tablespoon.agent.events.EventSender;
import se.kth.tablespoon.agent.general.ConfigurationHandler;

import se.kth.tablespoon.agent.listeners.CollectlListener;
import se.kth.tablespoon.agent.listeners.ConfigListener;
import se.kth.tablespoon.agent.util.Sleep;

public class Start {
  
  private static final int WAIT_FOR_QUEUE_TO_FILL_TIME = 1000;
  private static final int WAIT_FOR_THREAD_TO_DIE_TIME = 500;
  private static final int READ_QUEUE_TIME = 10;
  private static RiemannClient riemannClient;
  private static CollectlListener collectlListener;
  private static ConfigListener configListener;
  private static Configuration config;
  private static Thread collectlThread;
  private static Thread configThread;
  private final static Logger slf4jLogger = LoggerFactory.getLogger(Start.class);
  
  public static void main(String[] args) {
    setUpHook();
    ConfigurationHandler ch = new ConfigurationHandler();
    ch.loadNewConfiguration();
    config = ch.getConfig();
    collectlListener = new CollectlListener(config);
    ch.setCollectlListener(collectlListener);
    configListener = new ConfigListener(ch);
    startCollectlThread();
    startConfigThread();
    connect(0);
    System.exit(0); //will trigger the exit handler which kills the thread
  }
  
  private static void startCollectlThread() {
    collectlThread = new Thread(collectlListener);
    collectlThread.start();
    while (collectlListener.queueIsEmpty()) {
      slf4jLogger.info("waiting...");
      Sleep.now(WAIT_FOR_QUEUE_TO_FILL_TIME);
    }
  }
  
  private static void startConfigThread() {
    configThread = new Thread(collectlListener);
    configThread.start();
  }
  
  private static void connect(int tries) {
    EventSender es;
    try {
      riemannClient = RiemannClient.tcp(config.getRiemannHost(),
          config.getRiemannPort());
      riemannClient.connect();
      slf4jLogger.info("Established connection with host:" +
          config.getRiemannHost() + " port:" + config.getRiemannPort());
      tries = 0; //resetting number of tries
      es = new EventSender(collectlListener.getRowQueue(),
          collectlListener.getEventLayouts(),
          riemannClient,
          config);
      while (true) {
        // it apparently needs some headroom.
        Sleep.now(READ_QUEUE_TIME);
        if (!collectlListener.queueIsEmpty()) {
          es.sendMetrics();
        }
      }
    } catch (IOException e) {
      riemannClient.close();
      slf4jLogger.info("Connection with server could not be established.");
      if (tries < config.getRiemannReconnectionTries()) {
        slf4jLogger.info("Waiting for " +
            Math.round(config.getRiemannReconnectionTime() / 1000) +
            " seconds and attempting to connect again...");
        Sleep.now(config.getRiemannReconnectionTime());
        connect(++tries);
      }
    }
  }
  
  private static void setUpHook() {
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
      public void run() {
        if (collectlListener != null) {
          collectlListener.requestInterrupt();
          while (collectlThread.isAlive()) {
            slf4jLogger.info("waiting for thread to die...");
            Sleep.now(WAIT_FOR_THREAD_TO_DIE_TIME);
          }
        }
        if (configListener != null) {
          configListener.requestInterrupt();
          while (configThread.isAlive()) {
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
