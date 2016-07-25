package se.kth.tablespoon.client.topics;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.client.general.Groups;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import se.kth.tablespoon.client.broadcasting.AgentBroadcaster;
import se.kth.tablespoon.client.broadcasting.BroadcastException;
import se.kth.tablespoon.client.util.Time;


public class TopicStorage {
  
  private final ConcurrentHashMap<String, Topic> storage = new ConcurrentHashMap<>();
  private final ConcurrentLinkedQueue<String> topicsScehduledForRemoval =  new ConcurrentLinkedQueue<>();
  private final Groups groups;
  private boolean changed = false;
  private final static Logger slf4jLogger = LoggerFactory.getLogger(TopicStorage.class);


  public TopicStorage(Groups groups) {
    this.groups = groups;
  }
  
  public void add(Topic topic) throws IOException {
    topic.generateJson();
    storage.put(topic.getUniqueId(), topic);
  }
  
  public Topic get(String uniqueId) throws MissingTopicException {
    Topic topic = storage.get(uniqueId);
    if (topic==null) throw new MissingTopicException();
    return topic;
  }
  
  public void remove(String uniqueId) throws MissingTopicException {
    if (storage.get(uniqueId)==null) throw new MissingTopicException();
    topicsScehduledForRemoval.add(uniqueId);
  }
  
  public void broadcastRemoval(AgentBroadcaster broadcaster) throws IOException, BroadcastException {
    String uniqueId;
    while ((uniqueId = topicsScehduledForRemoval.peek()) != null) {
      Topic topic = storage.get(uniqueId);
      if (topic != null) broadcaster.sendToMachines(topic.getMachines(), topic.getRemovalJson(), uniqueId);
      slf4jLogger.info("Topic " + uniqueId + " was removed from TopicStorage. ");
      storage.remove(uniqueId);
      topicsScehduledForRemoval.poll();
    }
  }
  
  public void notifyBroadcaster() {
    synchronized (this) {
      stateHasChanged(true);
      this.notify();
    }
    slf4jLogger.info("State of TopicStorage has changed and broadcasters have been notified. ");
  }
  
  public void stateHasChanged(boolean changed) {
    this.changed = changed;
  }
  
  public boolean uniqueIdExists(String uniqueId) {
    return storage.containsKey(uniqueId);
  }
  
  public void clean() {
    slf4jLogger.info("Clean of TopicStorage was initialised. ");
    Iterator<Entry<String, Topic>> entries = storage.entrySet().iterator();
    long now = Time.now();
    groups.takeSnapshop();
    while (entries.hasNext()) {
      Entry<String, Topic> entry = entries.next();
      Topic topic = entry.getValue();
      topic.updateMachineState(groups);
      if ((topic.getDuration() > 0 && (topic.getStartTime() + topic.getDuration()) < now)) {
        slf4jLogger.info("Topic '" + topic.toString() + "' was removed due to expired duration. ");
        entries.remove();
      }
      else if(topic.hasNoLiveMachines()) {
        slf4jLogger.info("Topic '" + topic.toString() + "' was removed due to having no live machines. ");
        entries.remove();
      }
    }
    slf4jLogger.info("Clean of TopicStorage was completed. ");
  }
  
  public Collection<Topic> getTopics() {
    return storage.values();
  }
  
  public boolean isEmpty() {
    return storage.isEmpty();
  }
  
  public boolean isStorageChanged() {
    return changed;
  }
  
  @SuppressWarnings("empty-statement")
  public String generateUniqueId() {
    String uniqueId;
    while (uniqueIdExists(uniqueId = UUID.randomUUID().toString()));
    return uniqueId;
  }
  
  
}
