package se.kth.tablespoon.client.broadcasting;

import io.riemann.riemann.Proto.Event;
import io.riemann.riemann.client.RiemannClient;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import se.kth.tablespoon.client.api.Subscriber;
import se.kth.tablespoon.client.topics.Topic;
import se.kth.tablespoon.client.util.Time;

class RiemannEventFetcher {
  
  private final Subscriber subscriber;
  private final Topic topic;
  private long lastQueryTimeMs = 0;
  private final LinkedHashSet<MachineTime> seen;
  private final int SENT_EVENTS_QUEUE_SIZE = 2000;
  
  RiemannEventFetcher(Subscriber subscriber, Topic topic) {
    this.seen = new LinkedHashSet<>();
    this.subscriber = subscriber;
    this.topic = topic;
  }
  
  boolean shouldQuery() {
    return Time.nowMs() - lastQueryTimeMs >= (topic.getSendRate() * 1000) / 2;
  }
  
  void queryRiemannAndSend(RiemannClient rClient) throws IOException {
    lastQueryTimeMs = Time.nowMs();
    List<Event> events =  rClient.query("service = \"" + topic.getUniqueId() + "\"").deref();
    sendEvents(events);
    cleanSeen();
  }
  
  private void sendEvents(List<Event> events) {
    for (Event event : events) {
      MachineTime mt = new MachineTime(event.getHost(), event.getTime());
      if (seen.add(mt)) continue;
      subscriber.onEventArrival(EventConverter.changeFormat(event, topic));
    }
  }
  
  private void cleanSeen() {
    Iterator<MachineTime> iterator = seen.iterator();
    while (iterator.hasNext() && seen.size() >= SENT_EVENTS_QUEUE_SIZE) {
      iterator.remove();
    }
  }
  
  
  
}
