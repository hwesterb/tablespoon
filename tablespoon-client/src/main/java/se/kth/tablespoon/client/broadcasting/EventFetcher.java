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
import se.kth.tablespoon.client.events.EventConverter;
import se.kth.tablespoon.client.topics.Topic;
import se.kth.tablespoon.client.util.Time;

/**
 *
 * @author henke
 */
public class EventFetcher {
  
  private final Subscriber subscriber;
  private final Topic topic;
  private long lastQuery = 0;
  private final LinkedHashSet<MachineTime> sentEvents;
  private final int SENT_EVENTS_QUEUE_SIZE = 2000;
  
  public EventFetcher(Subscriber subscriber, Topic topic) {
    this.sentEvents = new LinkedHashSet<>();
    this.subscriber = subscriber;
    this.topic = topic;
  }
  
  public boolean shouldQuery() {
    return Time.now() - lastQuery >= topic.getSendRate();
  }
  
  public void queryRiemann(RiemannClient rClient) throws IOException {
    lastQuery = Time.now();
    List<Event> events =  rClient.query("service = \"" + topic.getUniqueId() + "\"").deref();
    for (Event event : events) {
      MachineTime mt = new MachineTime(event.getHost(), event.getTime());
      if (sentEvents.contains(mt)) continue;
      sentEvents.add(mt);
      subscriber.onEventArrival(EventConverter.changeFormat(event, topic));
    }
    cleanSentEvents();
  }
  
  private void cleanSentEvents() {
    Iterator<MachineTime> iterator = sentEvents.iterator();
    while (iterator.hasNext() && sentEvents.size() >= SENT_EVENTS_QUEUE_SIZE) {
      iterator.remove();
    }
  }
  
  
  
}
