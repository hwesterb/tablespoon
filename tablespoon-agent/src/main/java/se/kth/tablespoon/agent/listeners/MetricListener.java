package se.kth.tablespoon.agent.listeners;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.agent.events.Configuration;
import se.kth.tablespoon.agent.metrics.Metric;
import se.kth.tablespoon.agent.metrics.MetricFactory;
import se.kth.tablespoon.agent.metrics.MetricLayout;
import se.kth.tablespoon.agent.util.Time;

public abstract class MetricListener implements Runnable {
  
  protected final Queue<Metric> globalQueue = new LinkedList<>();
  protected Process process;
  protected BufferedReader br;
  protected MetricLayout[] mls;
  protected boolean headersDefined = false;
  protected boolean interruptRequest = false;
  protected boolean restartRequest = false;
  protected final Configuration config = Configuration.getInstance();
  protected final Logger slf4jLogger = LoggerFactory.getLogger(MetricListener.class);
  
  @Override
  public void run() {
    collectCycle();
    slf4jLogger.info("Ending metricListener-thread.");
  }
  
  public abstract void collectCycle();
  
  protected void addMetricToGlobal(String line) {
    ArrayList<Metric> metrics = MetricFactory.createMetrics(line, mls, config);
    createCustomMetrics(metrics);
    synchronized (globalQueue) {
      globalQueue.addAll(metrics);
    }
  }
  
  protected abstract void createCustomMetrics(ArrayList<Metric> metrics);
  
  protected void expireOldMetrics() {
    int i = 0;
    long ttl = config.getRiemannEventTtl();
    synchronized (globalQueue) {
      while (globalIsEmpty() == false) {
        Metric metric = globalQueue.peek();
        if (Time.now() - metric.getTimeStamp() > ttl) {
          globalQueue.remove();
          i++;
        } else {
          break;
        }
      }
    }
    if (i > 0) slf4jLogger.info("Expired " + i + " metrics.");
  }
  
  public void requestInterrupt() {
    restartRequest = false;
    interruptRequest = true;
  }
  
  public void requestRestart() {
    restartRequest = true;
    slf4jLogger.info("Attempting to restart collectl.");
  }
  
  public Queue<Metric> getGlobalQueue() {
    return globalQueue;
  }
  
  public MetricLayout[] getEventLayouts() {
    return mls;
  }
  
  public boolean globalIsEmpty() {
    return globalQueue.isEmpty();
  }
  
  public boolean isRestarting() {
    return restartRequest;
  }
  
}
