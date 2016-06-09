package se.kth.tablespoon.client.broadcasting;

import java.util.Iterator;
import java.util.LinkedHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.client.api.Subscriber;
import se.kth.tablespoon.client.topics.Topic;
import se.kth.tablespoon.client.util.Time;

public abstract class EventFetcher implements Runnable {
  
  protected final Subscriber subscriber;
  protected final Topic topic;
  protected long lastQueryTimeMs = 0;
  protected final LinkedHashSet<MachineTime> passedThrough;
  protected final int SENT_EVENTS_QUEUE_SIZE = 2000;
  protected final static Logger slf4jLogger = LoggerFactory.getLogger(EventFetcher.class);
  
  
  public EventFetcher(Subscriber subscriber, Topic topic) {
    this.passedThrough = new LinkedHashSet<>();
    this.subscriber = subscriber;
    this.topic = topic;
  }
  
  public boolean shouldQuery() {
    return Time.nowMs() - lastQueryTimeMs >= (topic.getSendRate() * 1000) / 2;
  }
  
  public abstract void query();
  
  protected void clean() {
    Iterator<MachineTime> iterator = passedThrough.iterator();
    while (iterator.hasNext() && passedThrough.size() >= SENT_EVENTS_QUEUE_SIZE) {
      iterator.remove();
    }
  }
  
}