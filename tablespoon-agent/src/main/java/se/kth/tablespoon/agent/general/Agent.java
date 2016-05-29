/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.agent.general;

import se.kth.tablespoon.agent.events.Configuration;
import com.aphyr.riemann.client.RiemannClient;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.agent.events.Topics;
import se.kth.tablespoon.agent.file.TopicLoader;
import se.kth.tablespoon.agent.listeners.MetricListener;
import se.kth.tablespoon.agent.main.Start;
import se.kth.tablespoon.agent.util.Time;

public class Agent {
  
  private final Configuration config = Configuration.getInstance();
  private final MetricListener metricListener;
  private final TopicLoader topicLoader;
  private final Topics topics;
  private RiemannClient riemannClient;
  private EventSender es;
  
  public RiemannClient getRiemannClient() {
    return riemannClient;
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
  
<<<<<<< Updated upstream
  private void connect() throws IOException {
    riemannClient = RiemannClient.tcp(config.getRiemannHost(),
||||||| merged common ancestors
  private void sendCycle() throws IOException {
    while (true) {
      topicLoader.readTopicFiles();
      ArrayList<RiemannEvent> events = new ArrayList<>();
      synchronized (metricListener.getGlobalQueue()) {
        while (metricListener.globalIsEmpty() == false) {
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
    Metric metric = metricListener.getGlobalQueue().poll();
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
=======
  private void sendCycle() throws IOException {
    while (true) {
      topicLoader.readTopicFiles();
      ArrayList<Metric> metrics = new ArrayList<>();
      synchronized (metricListener.getGlobalQueue()) {
        while (metricListener.globalIsEmpty() == false) {
          metrics.add(metricListener.getGlobalQueue().poll());
        }
      }
      ArrayList<RiemannEvent> events = new ArrayList<>();
      events.addAll(extract(metrics));
      List<Event> batch = new ArrayList<>();
      for (RiemannEvent event : events) {
        batch.add(event.prepare(rbc.client));
      }
      if (batch.isEmpty()) Time.sleep(500);
      else sendBatch(batch);
    }
  }
  
  private ArrayList<RiemannEvent> extract(ArrayList<Metric> metrics) throws IOException {
    ArrayList<RiemannEvent> riemannEvents = new ArrayList<>();
    for (Metric metric : metrics) {
      ArrayList<Topic> relevant = topics.getRelevantTopicsBeloningToIndex(metric.getCollectIndex());
      if (relevant.isEmpty()) continue;
      topics.clean(metric, relevant);
      riemannEvents.addAll(topics.extractRiemannEvents(metric, relevant));
    }
    return riemannEvents;
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
>>>>>>> Stashed changes
        config.getRiemannPort());
    riemannClient.connect();
    this.es = new EventSender(metricListener.getGlobalQueue(), riemannClient, topics);
    slf4jLogger.info("Established connection with host:"
        + config.getRiemannHost() + " port:" + config.getRiemannPort());
  }
  
  private boolean reconnect(int tries) {
    riemannClient.close();
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
  
  private void sendCycle() throws IOException {
    while (true) {
      topicLoader.readTopicFiles();
      Time.sleep(500);
      synchronized (metricListener.getGlobalQueue()) {
        while (metricListener.globalIsEmpty() == false) {
          es.sendMetric();
        }
      }
    }
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
    }
  }
  
  public void closeRiemannClient() {
    if (riemannClient != null) {
      if (riemannClient.isConnected()) {
        riemannClient.close();
        slf4jLogger.info("Closed the connection with the Riemann client.");
      }
    }
  }
}
