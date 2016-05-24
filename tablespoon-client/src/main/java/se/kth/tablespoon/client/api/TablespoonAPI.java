/*
* To getAndChange this license header, choose License Headers in Project Properties.
* To getAndChange this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.api;

import se.kth.tablespoon.client.broadcasting.SubscriberBroadcaster;
import se.kth.tablespoon.client.general.Group;
import se.kth.tablespoon.client.events.Threshold;
import se.kth.tablespoon.client.topics.ThresholdException;
import se.kth.tablespoon.client.topics.Topic;
import se.kth.tablespoon.client.topics.TopicFactory;
import se.kth.tablespoon.client.topics.TopicStorage;
import se.kth.tablespoon.client.events.EventType;
import se.kth.tablespoon.client.events.Resource;
import se.kth.tablespoon.client.general.Groups;
import se.kth.tablespoon.client.topics.MissingTopicException;
import se.kth.tablespoon.client.topics.TopicRemovalException;


/**
 * Tablespoon API for creating, changing and removing topics. Topics defines events
 * which are system measurements. Events are sent to event stream processor and filtered
 * according to the content of the topic.
 */
public class TablespoonAPI {
  
  private TopicStorage storage;
  private Groups groups;
  private SubscriberBroadcaster sb;
  private static TablespoonAPI instance = null;
  
  // TODO: make into Singleton.
  public static TablespoonAPI getInstance() {
    if(instance == null) {
      instance = new TablespoonAPI();
    }
    return instance;
  }
  
  protected TablespoonAPI() { }
  
  public void prepareAPI(TopicStorage storage, Groups groups, SubscriberBroadcaster sb) {
    this.storage = storage;
    this.groups = groups;
    this.sb = sb;
  }
  
  /**
   * This call creates a new topic which applies to one group. No thresholds are
   * active upon creation. This can be changed with the <code>changeTopic</code>
   * call.
   * @param subscriber A <code>Subscriber</code> which receives <code>TablespoonEvent</code>.
   * @param groupId Id which specifies a group.
   * @param eventType Specifies how to gather and filter information.
   * @param resource Type of resource which should be collected.
   * @param duration Duration of a topic. Will expire automatically after duration.
   * Set value 0 for topic without time bound.
   * @param sendRate The rate at which the agent sends messages.
   * @return An unique id which specifies the topic.
   */
  public String createTopic(Subscriber subscriber, String groupId, EventType eventType,
      Resource resource, int duration, int sendRate) {
    Topic topic = registerNewTopic(groupId, eventType, resource, duration, sendRate);
    topic.unlock();
    sb.registerSubscriber(subscriber, topic);
    storage.notifyBroadcaster();
    return topic.getUniqueId();
  }
  
  /**
   * This call creates a new topic which applies to one group. One thresholds are
   * active upon creation. This can be changed with the <code>changeTopic</code>
   * call.
   * @param subscriber A <code>Subscriber</code> which receives <code>TablespoonEvent</code>.
   * @param groupId Id which specifies a group.
   * @param eventType Specifies how to gather and filter information.
   * @param resource Type of resource which should be collected.
   * @param duration Duration of a topic. Will expire automatically after duration.
   * Set value 0 for topic without time bound.
   * @param sendRate The rate at which the agent sends messages.
   * @param threshold A <code>Threshold</code> that determines the percentage or
   * percentile when filtering events.
   * @return An unique id which specifies the topic.
   */
  public String createTopic(Subscriber subscriber, String groupId, EventType eventType,
      Resource resource, int duration, int sendRate, Threshold threshold) {
    Topic topic = registerNewTopic(groupId, eventType, resource, duration, sendRate);
    topic.setHigh(threshold);
    topic.unlock();
    sb.registerSubscriber(subscriber, topic);
    storage.notifyBroadcaster();
    return topic.getUniqueId();
  }
  
  /**
   * This call creates a new topic which applies to one group. Two thresholds are
   * active upon creation. This can be changed with the <code>changeTopic</code>
   * call.
   * @param subscriber A <code>Subscriber</code> which receives <code>TablespoonEvent</code>.
   * @param groupId Id which specifies a group.
   * @param eventType Specifies how to gather and filter information.
   * @param resource Type of resource which should be collected.
   * @param duration Duration of a topic. Will expire automatically after duration.
   * Set value 0 for topic without time bound.
   * @param sendRate The rate at which the agent sends messages.
   * @param high A <code>Threshold</code> that determines the percentage or
   * percentile when filtering events.
   * @param low A <code>Threshold</code> that determines the percentage or
   * percentile when filtering events.
   * @return An unique id which specifies the topic.
   * @throws ThresholdException Thrown if low >= high, or if <code>Comparator</code>
   * are incompatible.
   */
  public String createTopic(Subscriber subscriber, String groupId, EventType eventType,
      Resource resource, int duration, int sendRate, Threshold high, Threshold low) throws ThresholdException {
    Topic topic = registerNewTopic(groupId, eventType, resource, duration, sendRate);
    topic.setHigh(high);
    topic.setLow(low);
    topic.unlock();
    sb.registerSubscriber(subscriber, topic);
    storage.notifyBroadcaster();
    return topic.getUniqueId();
  }
  
  private Topic registerNewTopic(String groupId, EventType eventType, Resource resource,
      int duration, int sendRate) {
    Group group = groups.get(groupId);
    Topic topic = TopicFactory.create(storage, resource, eventType, sendRate, group);
    if (duration > 0) topic.setDuration(duration);
    topic.setSendRate(sendRate);
    return topic;
  }
  
  /**
   * This call changes a topic. It will block if the specific
   * topic is currently being handled.
   * @param uniqueId An unique id which specifies the topic.
   * @param threshold A <code>Threshold</code> that determines the percentage or
   * percentile when filtering events.
   * @throws MissingTopicException Thrown if the topic is not present in the storage.
   */
  public void changeTopic(String uniqueId, Threshold threshold) throws MissingTopicException {
    Topic topic = storage.getAndChange(uniqueId);
    topic.setHigh(threshold);
    topic.unlock();
    storage.notifyBroadcaster();
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
  public void changeTopic(String uniqueId, Threshold high, Threshold low)
      throws ThresholdException, MissingTopicException {
    Topic topic = storage.getAndChange(uniqueId);
    topic.setHigh(high);
    topic.setLow(low);
    topic.unlock();
    storage.notifyBroadcaster();
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
    storage.notifyBroadcaster();
  }
  
}
