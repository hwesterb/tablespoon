package se.kth.tablespoon.agent.events;

import se.kth.tablespoon.agent.metrics.Metric;
import java.io.IOException;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aphyr.riemann.client.RiemannClient;

import se.kth.tablespoon.agent.general.Configuration;

public class EventSender {
  
  
  private final Queue<Metric> metricQueue;
  private final RiemannClient rClient;
  private final Configuration config;
  private final Logger slf4jLogger = LoggerFactory.getLogger(EventSender.class);
  
  public EventSender(Queue<Metric> metricQueue, RiemannClient rClient, Configuration config) {
    this.config = config;
    this.metricQueue = metricQueue;
    this.rClient = rClient;
  }
  
  public void sendMetrics() throws IOException {
    
    Metric metric = metricQueue.poll();
    EventDefinition ed = config.getSubscriptions().get(metric.getCollectIndex());
    
    // This metric is not part of a subscription.
    if (ed == null) return;
    
    // This metric has a duration which has ended.
    if (durationHasEnded(metric, ed)) {
      // Write null to remove subscription.
      config.getSubscriptions().put(ed.getCollectlIndex(), null);
      return;
    }
    
    rClient.event().
        service(metric.getSource().toString()).
        description(metric.getName()).
        tag(metric.getFormat().toString()).
        metric(metric.getValue()).
        time(metric.getTimeStamp()).
        ttl(config.getRiemannEventTtl()).
        send().
        deref(config.getRiemannDereferenceTime(), java.util.concurrent.TimeUnit.MILLISECONDS);
  }
  
  
  private boolean durationHasEnded(Metric metric, EventDefinition ed) {
    if (ed.hasDuration()) {
      if (ed.hasStarted()) {
        long now = System.currentTimeMillis() / 1000L;
        if ((now - metric.getTimeStamp()) > ed.getDuration()) {
          return true;
        }
      } else {
        ed.setStartTime(metric.getTimeStamp());
        ed.setStarted(true);
      }
    }
    return false;
  }
  
}
