package se.kth.tablespoon.agent.general;

import io.riemann.riemann.Proto.Event;
import io.riemann.riemann.Proto.Msg;
import io.riemann.riemann.client.IPromise;
import io.riemann.riemann.client.IRiemannClient;
import io.riemann.riemann.client.RiemannBatchClient;
import io.riemann.riemann.client.RiemannClient;
import io.riemann.riemann.client.UnsupportedJVMException;
import se.kth.tablespoon.agent.events.Configuration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.agent.events.RiemannEvent;
import se.kth.tablespoon.agent.events.Topic;
import se.kth.tablespoon.agent.events.Topics;
import se.kth.tablespoon.agent.file.TopicLoader;
import se.kth.tablespoon.agent.listeners.MetricListener;
import se.kth.tablespoon.agent.main.Start;
import se.kth.tablespoon.agent.metrics.Metric;
import se.kth.tablespoon.agent.util.Time;

public class Agent {
  
  private final Configuration config = Configuration.getInstance();
  private final MetricListener metricListener;
  private final TopicLoader topicLoader;
  private final Topics topics;
  private RiemannBatchClient rbc;
  
  public IRiemannClient getRiemannClient() {
    return rbc;
  }
  private final Logger slf4jLogger = LoggerFactory.getLogger(Start.class);
  
  public Agent(MetricListener metricListener, Topics topics) {
    this.metricListener = metricListener;
    this.topicLoader = new TopicLoader(topics);
    this.topics = topics;
  }
  
  public void start() {
    agentCycle(0);
  }
  
  private void sendCycle() throws IOException {
    while (true) {
      topicLoader.readTopicFiles();
      ArrayList<RiemannEvent> events = new ArrayList<>();
      synchronized (metricListener.getMetricQueue()) {
        while (metricListener.metricQueueIsEmpty() == false) {
          events.addAll(extract());
        }
      }
      List<Event> batch = new ArrayList<>();
      for (RiemannEvent event : events) {
        batch.add(event.prepare(rbc.client));
      }
      if (batch.isEmpty()) Time.sleep(500);
      else sendBatch(batch);
    }
  }
  
  private ArrayList<RiemannEvent> extract() throws IOException {
    Metric metric = metricListener.getMetricQueue().poll();
    ArrayList<Topic> relevant = topics.getRelevantTopicsBeloningToIndex(metric.getCollectIndex());
    if (relevant.isEmpty()) return new ArrayList<>();
    topics.clean(metric, relevant);
    return topics.extractRiemannEvents(metric, relevant);
  }
  
  private void sendBatch(List<Event> batch) throws IOException {
    try {
      IPromise<Msg> promise = rbc.client.sendEvents(batch);
      Msg msg = promise.deref(config.getRiemannDereferenceTime(),
          java.util.concurrent.TimeUnit.MILLISECONDS);
      if (msg.hasOk() == false) throw new IOException("Timed out after "
          + config.getRiemannDereferenceTime() + ".");
    } catch (IOException ex) {
      slf4jLogger.error(ex.getMessage());
      if (rbc.isConnected()) sendBatch(batch);
      else {
        slf4jLogger.error("Batch of size " + batch.size()
            + " discarded.");
        throw new IOException();
      }
    }
  }
  
  private void connect() throws IOException, UnsupportedJVMException {
    RiemannClient riemannClient = RiemannClient.tcp(config.getRiemannHost(),
        config.getRiemannPort());
    rbc = new RiemannBatchClient(riemannClient);
    rbc.connect();
    slf4jLogger.info("Established connection with host:"
        + config.getRiemannHost() + " port:" + config.getRiemannPort());
  }
  
  private boolean reconnect(int tries) {
    rbc.close();
    slf4jLogger.info("Connection with server could not be established.");
    if (tries < config.getRiemannReconnectionTries()) {
      slf4jLogger.info("Waiting for "
          + Math.round(config.getRiemannReconnectionTime() / 1000)
          + " seconds before attempting to connect again...");
      Time.sleep(config.getRiemannReconnectionTime());
      return true;
    }
    return false;
  }
  
  private void agentCycle(int tries) {
    try {
      connect();
      //resetting number of tries if connection was established
      tries = 0;
      sendCycle();
    } catch (IOException e) {
      if (reconnect(tries)) {
        agentCycle(++tries);
      }
    } catch (UnsupportedJVMException ex) {
      slf4jLogger.error(ex.getMessage());
      System.exit(0);
    }
  }
  
  public void closeRiemannClient() {
    if (rbc != null) {
      if (rbc.isConnected()) {
        rbc.close();
        slf4jLogger.info("Closed the connection with the Riemann client.");
      }
    }
  }
  
}