package se.kth.tablespoon.agent.listeners;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.agent.file.Configuration;
import se.kth.tablespoon.agent.metrics.Metric;
import se.kth.tablespoon.agent.metrics.MetricFactory;
import se.kth.tablespoon.agent.metrics.MetricLayout;

public abstract class MetricListener implements Runnable {
  
  protected final Queue<Metric> metricQueue = new LinkedList<>();
  protected Process process;
  protected BufferedReader br;
  protected MetricLayout[] mls;
  protected boolean headersDefined = false;
  protected boolean interruptRequest = false;
  protected boolean restartRequest = false;
  protected final Configuration config;
  protected final Logger slf4jLogger = LoggerFactory.getLogger(MetricListener.class);
  
  public MetricListener(Configuration config) {
    this.config = config;
  }
  
  @Override
  public void run() {
    collectCycle();
    slf4jLogger.info("Ending metricListener-thread.");
  }
  
  public abstract void collectCycle();
  

  protected void addMetricToQueue(String line) {
    Metric[] events = MetricFactory.createMetrics(line, mls, config);
    metricQueue.addAll(Arrays.asList(events));
  }
  protected void emptyOldMetrics() {
    Metric metric = metricQueue.peek();
    long ttl = config.getRiemannEventTtl();
    long now = System.currentTimeMillis() / 1000L;
    if (now - metric.getTimeStamp() > ttl) {
      synchronized (metricQueue) {
        metricQueue.remove();
      }
      slf4jLogger.info("Metric was too old and discarded.");
      emptyOldMetrics();
    }
    
  }
  
  public void requestInterrupt() {
    restartRequest = false;
    interruptRequest = true;
  }
  
  public void requestRestart() {
    restartRequest = true;
    slf4jLogger.info("Attempting to restart collectl.");
  }
  
  public Queue<Metric> getMetricQueue() {
    return metricQueue;
  }
  
  public MetricLayout[] getEventLayouts() {
    return mls;
  }
  
  public boolean queueIsEmpty() {
    return metricQueue.isEmpty();
  }
  
  public boolean isRestarting() {
    return restartRequest;
  }
  
}
