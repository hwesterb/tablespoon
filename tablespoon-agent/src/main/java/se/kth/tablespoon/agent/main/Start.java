package se.kth.tablespoon.agent.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.agent.events.Topics;
import se.kth.tablespoon.agent.general.Agent;
import se.kth.tablespoon.agent.file.ConfigurationLoader;
import se.kth.tablespoon.agent.file.JsonException;

import se.kth.tablespoon.agent.listeners.CollectlListener;
import se.kth.tablespoon.agent.listeners.MetricListener;
import se.kth.tablespoon.agent.util.Sleep;

public class Start {
  
  private static final int WAIT_FOR_QUEUE_TO_FILL_TIME = 1000;
  private static final int WAIT_FOR_THREAD_TO_DIE_TIME = 500;
  private static MetricListener metricListener;
  private static Agent agent;
  private static Thread collectlThread;
  private final static Logger slf4jLogger = LoggerFactory.getLogger(Start.class);
  
  public static void main(String[] args) throws JsonException {
    slf4jLogger.info("Starting tablespoon agent.");
    (new ConfigurationLoader()).readConfigFile();
    metricListener = new CollectlListener();
    Topics topics = new Topics();
    startCollectlThread();
    agent = new Agent(metricListener, topics);
    setUpHook();
    agent.start();
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
        agent.closeRiemannClient();
      }
    }));
  }
  
}
