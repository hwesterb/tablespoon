package se.kth.tablespoon.agent.general;

import se.kth.tablespoon.agent.metrics.Metric;
import java.io.IOException;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aphyr.riemann.client.RiemannClient;
import java.util.ArrayList;

import se.kth.tablespoon.agent.events.Configuration;
import se.kth.tablespoon.agent.events.RelatedTopics;
import se.kth.tablespoon.agent.events.RiemannEvent;

public class EventSender {
  
  
  private final Queue<Metric> metricQueue;
  private final RiemannClient rClient;
  private final Configuration config = Configuration.getInstance();
  private final Logger slf4jLogger = LoggerFactory.getLogger(EventSender.class);
  
  public EventSender(Queue<Metric> metricQueue, RiemannClient rClient) {
    this.metricQueue = metricQueue;
    this.rClient = rClient;
  }
  
  public void sendMetrics() throws IOException {
    
    Metric metric = metricQueue.poll();
    RelatedTopics related = config.getRelatedTopicsBeloningToIndex(metric.getCollectIndex());
    if (related == null) return;    
    ArrayList<RiemannEvent> riemannEvents = related.extractRiemannEvents(metric);
      
     for (RiemannEvent riemannEvent : riemannEvents) {
       riemannEvent.sendMe(rClient, config.getRiemannDereferenceTime());
     }
  }
  
  

}
