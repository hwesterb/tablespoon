package se.kth.tablespoon.agent.main;

import com.aphyr.riemann.client.RiemannClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.agent.general.Configuration;
import se.kth.tablespoon.agent.general.Agent;
import se.kth.tablespoon.agent.general.ConfigurationLoader;

import se.kth.tablespoon.agent.listeners.CollectlListener;
import se.kth.tablespoon.agent.listeners.ConfigListener;
import se.kth.tablespoon.agent.listeners.MetricListener;
import se.kth.tablespoon.agent.util.Sleep;

public class Start {

  private static final int WAIT_FOR_QUEUE_TO_FILL_TIME = 1000;
  private static final int WAIT_FOR_THREAD_TO_DIE_TIME = 500;
   private static RiemannClient riemannClient;
  private static MetricListener metricListener;
  private static ConfigListener configListener;
  private static Configuration config;
  private static Agent agent;
  private static Thread collectlThread;
  private static Thread configThread;
  private final static Logger slf4jLogger = LoggerFactory.getLogger(Start.class);

  public static void main(String[] args) {
    slf4jLogger.info("Starting tablespoon agent.");
    setUpHook();
    ConfigurationLoader ch = new ConfigurationLoader();
    ch.loadNewConfiguration();
    config = ch.getConfig();
    metricListener = new CollectlListener(config);
    ch.setMetricListener(metricListener);
    configListener = new ConfigListener(ch);
    startCollectlThread();
    startConfigThread();
    agent = new Agent(metricListener, config);
    System.exit(0); //will trigger the exit handler which kills the thread
  }

  private static void startCollectlThread() {
    collectlThread = new Thread(metricListener);
    collectlThread.start();
    while (metricListener.queueIsEmpty()) {
      slf4jLogger.info("waiting...");
      Sleep.now(WAIT_FOR_QUEUE_TO_FILL_TIME);
    }
  }

  private static void startConfigThread() {
    configThread = new Thread(configListener);
    configThread.start();
  }

  private static void setUpHook() {
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
      public void run() {
        if (metricListener != null) {
          metricListener.requestInterrupt();
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
        agent.closeRiemannClient();
      }
    }));
  }

}
