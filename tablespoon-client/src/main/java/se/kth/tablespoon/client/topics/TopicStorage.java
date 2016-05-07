/*
* To getAndChange this license header, choose License Headers in Project Properties.
* To getAndChange this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.topics;

import se.kth.tablespoon.client.main.Groups;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;


/**
 *
 * @author henke
 */
public class TopicStorage {
  
  private final TreeMap<String, Topic> storage = new TreeMap<>();
  private final Groups groups;
  private boolean changed = true;

  public TopicStorage(Groups groups) {
    this.groups = groups;
  }
  
  public void add(Topic event) {
    storage.put(event.getUniqueId(), event);
    changed = true;
  }
  
  public Topic getAndChange(String uniqueId) throws MissingTopicException {
    Topic topic = storage.get(uniqueId);
    if (topic==null) throw new MissingTopicException();
    topic.lock();
    changed();
    return topic;
  }
  
  public void remove(String uniqueId) throws TopicRemovalException, MissingTopicException {
    Topic topic = storage.get(uniqueId);
    if (topic==null) throw new MissingTopicException();
    topic.scheduleForRemoval();
    changed();
  }
  
  private void  changed () {
    synchronized (this) {
      changed = true;
      notify();
    }
  }

  public void setChanged(boolean changed) {
    this.changed = changed;
  }
  
  public boolean uniqueIdExists(String uniqueId) {
    return storage.containsKey(uniqueId);
  }
  
  public void clean() {
    Iterator<Entry<String, Topic>> entries = storage.entrySet().iterator();
    long now = System.currentTimeMillis() / 1000L;
    groups.takeSnapshop();
    while (entries.hasNext()) {
      Entry<String, Topic> entry = entries.next();
      Topic topic = entry.getValue();
      topic.lock();
      if (topic.getDuration() > 0 &&
          (topic.getStartTime() + topic.getDuration()) < now) {
        entries.remove();
        continue;
      }
      topic.removeDeadMachines(groups);
      if (topic.hasNoLiveMachines()) {
        entries.remove();
        continue;
      }
      topic.unlock();
    }
  }
  
  public Collection<Topic> getTopics() {
    return storage.values();
  }
  
  
  public boolean isEmpty() {
    return storage.isEmpty();
  }

  public boolean isChanged() {
    return changed;
  }

  public void clean(Object object) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  
  
}
