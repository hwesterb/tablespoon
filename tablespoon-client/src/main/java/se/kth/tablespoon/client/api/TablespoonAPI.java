/*
* To getAndChange this license header, choose License Headers in Project Properties.
* To getAndChange this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.api;

import se.kth.tablespoon.client.main.Group;
import se.kth.tablespoon.client.topics.ResourceType;
import se.kth.tablespoon.client.topics.Threshold;
import se.kth.tablespoon.client.topics.ThresholdException;
import se.kth.tablespoon.client.topics.Topic;
import se.kth.tablespoon.client.topics.TopicFactory;
import se.kth.tablespoon.client.topics.TopicStorage;
import se.kth.tablespoon.client.topics.EventType;
import se.kth.tablespoon.client.main.Groups;
import se.kth.tablespoon.client.topics.MissingTopicException;
import se.kth.tablespoon.client.topics.TopicRemovalException;


/**
 * Tablespoon API for creating, changing and removing topics. Topics defines events
 * which are system measurements. Events are sent to event stream processor and filtered
 * according to the content of the topic.
 */
public class TablespoonAPI {
  
  private final TopicStorage storage;
  private final Groups groups;
  
  public TablespoonAPI(TopicStorage storage, Groups groups) {
    this.storage = storage;
    this.groups = groups;
  }
  
  /**
   * This call creates a new topic which applies to one group. Two thresholds are
   * active upon creation. This can be changed with the <code>changeTopic</code>
   * call.
   * @param subscriber A <code>Subscriber</code> which receives <code>Event</code>.
   * @param groupId Id which specifies a group.
   * @param eventType Specifies how to gather and filter information.
   * @param resourceType Type of resource which should be collected.
   * @param duration Duration of a topic. Will expire automatically after duration.
   * Set value 0 for topic without time bound.
   */
  public void createTopic(Subscriber subscriber, String groupId, EventType eventType,
      ResourceType resourceType, int duration) {
    Topic topic = registerNewTopic(groupId, eventType, resourceType, duration);
    topic.unlock();
    subscriber.setUniqueId(topic.getUniqueId());
  }
  
  /**
   * This call creates a new topic which applies to one group. Two thresholds are
   * active upon creation. This can be changed with the <code>changeTopic</code>
   * call.
   * @param subscriber A <code>Subscriber</code> which receives <code>Event</code>.
   * @param groupId Id which specifies a group.
   * @param eventType Specifies how to gather and filter information.
   * @param resourceType Type of resource which should be collected.
   * @param duration Duration of a topic. Will expire automatically after duration.
   * Set value 0 for topic without time bound.
   * @param high A <code>Threshold</code> that determines the percentage or
   * percentile when filtering events.
   */
  public void createTopic(Subscriber subscriber, String groupId, EventType eventType,
      ResourceType resourceType, int duration, Threshold high) {
    Topic topic = registerNewTopic(groupId, eventType, resourceType, duration);
    topic.setHigh(high);
    topic.unlock();
    subscriber.setUniqueId(topic.getUniqueId());
  }
  
  /**
   * This call creates a new topic which applies to one group. Two thresholds are
   * active upon creation. This can be changed with the <code>changeTopic</code>
   * call.
   * @param subscriber A <code>Subscriber</code> which receives <code>Event</code>.
   * @param groupId Id which specifies a group.
   * @param eventType Specifies how to gather and filter information.
   * @param resourceType Type of resource which should be collected.
   * @param duration Duration of a topic. Will expire automatically after duration.
   * Set value 0 for topic without time bound.
   * @param high A <code>Threshold</code> that determines the percentage or
   * percentile when filtering events.
   * @param low A <code>Threshold</code> that determines the percentage or
   * percentile when filtering events.
   * @throws ThresholdException Thrown if low >= high, or if <code>Comparator</code>
   * are incompatible.
   */
  public void createTopic(Subscriber subscriber, String groupId, EventType eventType, ResourceType resourceType, int duration,
      Threshold high, Threshold low) throws ThresholdException {
    Topic topic = registerNewTopic(groupId, eventType, resourceType, duration);
    topic.setHigh(high);
    topic.setLow(low);
    topic.unlock();
    subscriber.setUniqueId(topic.getUniqueId());
  }
  
  private Topic registerNewTopic(String groupId, EventType eventType, ResourceType resourceType, int duration) {
    Group group = groups.get(groupId);
    Topic topic = TopicFactory.create(storage, eventType, resourceType, group);
    if (duration > 0) topic.setDuration(duration);
    return topic;
  }
  
  /**
   * This call changes a topic. It will block if the specific
   * topic is currently being handled.
   * @param uniqueId An unique id which specifies the topic.
   * @param high A <code>Threshold</code> that determines the percentage or
   * percentile when filtering events.
   * @throws MissingTopicException Thrown if the topic is not present in the storage.
   */
  public void changeTopic(String uniqueId, Threshold high) throws MissingTopicException {
    Topic topic = storage.getAndChange(uniqueId);
    topic.setHigh(high);
    topic.unlock();
  }
  /**
   * This call changes a topic. It will block if the specific
   * topic is currently being handled.
   * @param uniqueId An unique id which specifies the topic.
   * @param high A <code>Threshold</code> that determines the percentage or
   * percentile when filtering events.
   * @param low A <code>Threshold</code> that determines the percentage or
   * percentile when filtering events.
   * @throws ThresholdException Thrown if low >= high, or if <code>Comparator</code>
   * are incompatible.
   * @throws MissingTopicException Thrown if the topic is not present in the storage.
   */
  public void changeTopic(String uniqueId, Threshold high, Threshold low) throws ThresholdException, MissingTopicException {
    Topic topic = storage.getAndChange(uniqueId);
    topic.setHigh(high);
    topic.setLow(low);
    topic.unlock();
  }
  
  /**
   * This call removes a topic.
   * @param uniqueId An unique id which specifies the topic.
   * @throws TopicRemovalException Thrown if attempting to remove topic with
   * existing duration. The topic will expire automatically.
   * @throws MissingTopicException Thrown if the topic is not present in the storage. 
   */
  public void removeTopic(String uniqueId) throws TopicRemovalException, MissingTopicException {
    storage.remove(uniqueId);
  }
  
}
