package se.kth.tablespoon.agent.general;

import io.riemann.riemann.client.RiemannClient;
import se.kth.tablespoon.agent.metrics.Metric;
import java.io.IOException;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;

import se.kth.tablespoon.agent.events.Configuration;
import se.kth.tablespoon.agent.events.Topics;
import se.kth.tablespoon.agent.events.RiemannEvent;
import se.kth.tablespoon.agent.events.Topic;

public class EventSender {
  
  
  private final Queue<Metric> metricQueue;
  private final RiemannClient rClient;
  private final Configuration config = Configuration.getInstance();
  private final Logger slf4jLogger = LoggerFactory.getLogger(EventSender.class);
  private final Topics topics; 
  
  public EventSender(Queue<Metric> metricQueue, RiemannClient rClient, Topics topics) {
    this.metricQueue = metricQueue;
    this.rClient = rClient;
    this.topics = topics;
  }
  
  public void sendMetric() throws IOException {
    Metric metric = metricQueue.poll();
    ArrayList<Topic> relevant = topics.getRelevantTopicsBeloningToIndex(metric.getCollectIndex());
    if (relevant.isEmpty()) return;
    topics.clean(metric, relevant);
    ArrayList<RiemannEvent> riemannEvents = topics.extractRiemannEvents(metric, relevant);
    
    //TODO: Aggregate riemannEvents with the same value and timeStamp.

    for (RiemannEvent riemannEvent : riemannEvents) {
      riemannEvent.sendMe(rClient, config.getRiemannDereferenceTime());
    }
  }  
}
