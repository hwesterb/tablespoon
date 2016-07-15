package se.kth.tablespoon.client.broadcasting;

import io.riemann.riemann.Proto.Event;
import io.riemann.riemann.client.RiemannClient;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.client.api.Subscriber;
import se.kth.tablespoon.client.topics.Topic;
import se.kth.tablespoon.client.util.Time;

public class RiemannEventFetcher extends EventFetcher {
  
  private final RiemannClient riemannClient;
  private final static Logger slf4jLogger = LoggerFactory.getLogger(RiemannEventFetcher.class);
  
  public RiemannEventFetcher(Subscriber subscriber, Topic topic, RiemannClient riemannClient) {
    super(subscriber, topic);
    this.riemannClient = riemannClient;
  }
  
  @Override
  public void query() {
    lastQueryTimeMs = Time.nowMs();
    try {
      List<Event> events = riemannClient.query("service = \"" + topic.getUniqueId() + "\"").
          deref(20, java.util.concurrent.TimeUnit.SECONDS);
      if(events.size() > 0) slf4jLogger.info(events.size() + " event(s) were fetched for topic " + topic.getUniqueId());
      notifySubscriber(events);
      clean();
    } catch (IOException ex) {
      slf4jLogger.debug(ex.getMessage());
    }
  }
  
  private void notifySubscriber(List<Event> events) {
    if (events==null) return; // TODO: better handling
    for (Event event : events) {
      if (event==null) return; // TODO: better handling
      MachineTime mt = new MachineTime(event.getHost(), event.getTime());
      if (passedThrough.add(mt) == false) continue;
      subscriber.onEventArrival(EventConverter.changeFormat(event, topic));
    }
  }
  
  @Override
  public void run() {
    query();
    topic.queryDone();
  }
  
}