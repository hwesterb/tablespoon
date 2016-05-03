package se.kth.tablespoon.agent.listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.agent.metrics.MetricFactory;

import se.kth.tablespoon.agent.metrics.MetricLayout;
import se.kth.tablespoon.agent.metrics.Metric;
import se.kth.tablespoon.agent.general.CollectlStringParser;
import se.kth.tablespoon.agent.general.Configuration;
import se.kth.tablespoon.agent.main.Start;

public class CollectlListener implements Runnable {
  
  
  private final Queue<Metric> metricQueue = new LinkedList<>();
  private Process process;
  private BufferedReader br;
  private MetricLayout[] mls;
  private boolean headersDefined = false;
  private boolean interruptRequest = false;
  private boolean restartRequest = false;
  private final Configuration config;
  private final Logger slf4jLogger = LoggerFactory.getLogger(Start.class);
  
  public CollectlListener(Configuration config) {
    this.config = config;
  }
  
  @Override
  public void run() {
    try {
      startCollectl();
    } catch (IOException e) {
      slf4jLogger.error(e.getMessage());
    }
    slf4jLogger.info("Ending collectl-thread.");
  }
  
  private void startCollectl() throws IOException {
    String[] cmd = {
      "/bin/sh",
      "-c",
      "collectl -P -o U -i " + config.getCollectlCollectionRate()
    };
    process = Runtime.getRuntime().exec(cmd);
    br = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line;
    while ((line = br.readLine()) != null) {
      slf4jLogger.info("Reading from collectl.");
      // define the headers
      if (!headersDefined) {
        if (line.startsWith("#")) {
          mls = CollectlStringParser.handleHeaders(line);
          headersDefined = true;
        }
        continue;
      }
     
      // add events to the queue
      Metric[] events = MetricFactory.createMetrics(line, mls, config);
      for (Metric event : events) {
        metricQueue.add(event);
      }
      
      emptyOld();
      
      if (interruptRequest) break;
      if (restartRequest) break;
    }
    stopCollectl();
    if (restartRequest) {
      restartRequest = false;
      slf4jLogger.info("Collectl is now restarted.");
      startCollectl();
    }
  }
  
  private void stopCollectl() throws IOException {
    br.close();
    process.destroy();
    headersDefined = false;
    slf4jLogger.info("Ending collectl-process.");
  }
  
  private void emptyOld() {
    Metric metric = metricQueue.peek();
    long ttl = config.getRiemannEventTtl();
    long now = System.currentTimeMillis() / 1000L;
    if (now - metric.getTimeStamp() > ttl) {
      synchronized (metricQueue) {
        metricQueue.remove();
      }
      slf4jLogger.info("Metric was too old and discarded.");
      emptyOld();
    }
    
  }
  
  public void requestInterrupt()  {
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
