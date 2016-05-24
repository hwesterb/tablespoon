/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.broadcasting;

import com.aphyr.riemann.Proto.Event;
import com.aphyr.riemann.client.RiemannClient;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import se.kth.tablespoon.client.api.Subscriber;
import se.kth.tablespoon.client.topics.Topic;
import se.kth.tablespoon.client.util.Time;

/**
 *
 * @author henke
 */
class RiemannEventFetcher {
  
  private final Subscriber subscriber;
  private final Topic topic;
  private long lastQueryTimeMs = 0;
  private final LinkedHashSet<MachineTime> sentEvents;
  private final int SENT_EVENTS_QUEUE_SIZE = 2000;
  
  RiemannEventFetcher(Subscriber subscriber, Topic topic) {
    this.sentEvents = new LinkedHashSet<>();
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
    cleanSentEvents();
  }
  
  private void sendEvents(List<Event> events) {
    for (Event event : events) {
      MachineTime mt = new MachineTime(event.getHost(), event.getTime());
      if (sentEvents.add(mt)) continue;
      subscriber.onEventArrival(EventConverter.changeFormat(event, topic));
    }
  }
  
  private void cleanSentEvents() {
    Iterator<MachineTime> iterator = sentEvents.iterator();
    while (iterator.hasNext() && sentEvents.size() >= SENT_EVENTS_QUEUE_SIZE) {
      iterator.remove();
    }
  }
  
  
  
}
