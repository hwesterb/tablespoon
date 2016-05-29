/*
* To getAndChange this license header, choose License Headers in Project Properties.
* To getAndChange this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.topics;

import se.kth.tablespoon.client.general.Groups;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import se.kth.tablespoon.client.util.Time;


public class TopicStorage {
  
  private final ConcurrentHashMap<String, Topic> storage = new ConcurrentHashMap<>();
  private final Groups groups;
  private boolean changed = false;
  
  public TopicStorage(Groups groups) {
    this.groups = groups;
  }
  
  public void add(Topic topic) {
    storage.put(topic.getUniqueId(), topic);
  }
  
  public Topic get(String uniqueId) throws MissingTopicException {
    Topic topic = storage.get(uniqueId);
    if (topic==null) throw new MissingTopicException();
    return topic;
  }
  
  public void remove(String uniqueId) throws TopicRemovalException, MissingTopicException {
    Topic topic = storage.get(uniqueId);
    if (topic==null) throw new MissingTopicException();
    topic.lock();
    topic.scheduledForRemoval();
    topic.unlock();
  }
  
  public void notifyBroadcaster() {
    synchronized (this) {
      storageHasChanged(true);
      this.notify();
    }
  }
  
  public void storageHasChanged(boolean changed) {
    this.changed = changed;
  }
  
  public boolean uniqueIdExists(String uniqueId) {
    return storage.containsKey(uniqueId);
  }
  
  public void clean() {
    Iterator<Entry<String, Topic>> entries = storage.entrySet().iterator();
    long now = Time.now();
    groups.takeSnapshop();
    while (entries.hasNext()) {
      Entry<String, Topic> entry = entries.next();
      Topic topic = entry.getValue();
      topic.updateMachineState(groups);
      if ((topic.getDuration() > 0 &&
          (topic.getStartTime() + topic.getDuration()) < now) ||
          topic.hasNoLiveMachines()) {
        entries.remove();
      }
    }
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
