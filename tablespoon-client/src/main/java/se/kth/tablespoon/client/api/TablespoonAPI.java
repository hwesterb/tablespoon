/*
* To getAndChange this license header, choose License Headers in Project Properties.
* To getAndChange this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.api;

import java.util.HashSet;
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
import se.kth.tablespoon.client.topics.GroupTopic;
import se.kth.tablespoon.client.topics.MissingTopicException;
import se.kth.tablespoon.client.topics.TopicRemovalException;

/**
 * Tablespoon API for creating, replacing, replicating and removing topics. Topics defines events
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
  
  public Submitter submitter() {
    return new Submitter();
  }
  
  protected TablespoonAPI() { }
  
  public void prepareAPI(TopicStorage storage, Groups groups, SubscriberBroadcaster sb) {
    this.storage = storage;
    this.groups = groups;
    this.sb = sb;
  }
  
  
  public class Submitter {
    
    private Subscriber subscriber;
    private String groupId;
    private EventType eventType;
    private Resource resource;
    private boolean durationSet = false;
    private int duration;
    private boolean sendRateSet = false;
    private int sendRate;
    private HashSet<String> machines;
    private Threshold high;
    private Threshold low;
    private String replacesTopicId;
    private boolean replicate;
    
    /**
     * This parameter is mandatory.
     * @param subscriber A <code>Subscriber</code> which receives <code>TablespoonEvent</code>.
     * @return <code>Submitter</code> for <code>TablespoonAPI</code>.
     */
    Submitter subscriber(Subscriber subscriber) {
      this.subscriber = subscriber;
      return this;
    }
    
    /**
     * This parameter is mandatory if not replicated.
     * @param eventType Specifies how to gather and filter information.
     * @return <code>Submitter</code> for <code>TablespoonAPI</code>.
     */
    Submitter eventType(EventType eventType) {
      this.eventType = eventType;
      return this;
    }
    
    /**
     * This parameter is mandatory if not replicated.
     * @param resource Type of resource which should be collected.
     * @return <code>Submitter</code> for <code>TablespoonAPI</code>.
     */
    Submitter resource(Resource resource) {
      this.resource = resource;
      return this;
    }
    
    /**
     * This parameter is mandatory if not replicated. Lowest possible send rate is 1.
     * @param sendRate The rate at which the agent sends messages.
     * @return <code>Submitter</code> for <code>TablespoonAPI</code>.
     */
    Submitter sendRate(int sendRate) {
      sendRateSet = true;
      this.sendRate = sendRate;
      return this;
    }
    
    /**
     * This parameter is not mandatory. If this parameter is not set the topic
     * will be active until removed.
     * @param duration Duration of a topic. Will expire automatically after duration.
     * @return <code>Submitter</code> for <code>TablespoonAPI</code>.
     */
    Submitter duration(int duration) {
      durationSet = true;
      this.duration = duration;
      return this;
    }
    
    /**
     * This parameter is mandatory if not replicated. Use either this or
     * {@link #machines(HashSet<String>) machines}.
     * @param groupId Id which specifies a group.
     * @return <code>Submitter</code> for <code>TablespoonAPI</code>.
     */
    Submitter groupId(String groupId) {
      this.machines = null;
      this.groupId = groupId;
      return this;
    }
    
    /**
     * This parameter is not mandatory if not replicated. Use either this or
     *  {@link #groupId(String) groupId}.
     * @param machines The machines where the topic should be active.
     * @return <code>Submitter</code> for <code>TablespoonAPI</code>.
     */
    Submitter machines(HashSet<String> machines) {
      this.groupId = null;
      this.machines = machines;
      return this;
    }
    
    /**
     * This parameter is not mandatory.
     * @param high A <code>Threshold</code> that determines the percentage or
     * percentile when filtering events.
     * @return
     */
    Submitter high(Threshold high) {
      this.high = high;
      return this;
    }
    
    /**
     * This parameter is not mandatory.
     * @param low A <code>Threshold</code> that determines the percentage or
     * percentile when filtering events.
     * @return
     */
    Submitter low(Threshold low) {
      this.low = low;
      return this;
    }
    
    Submitter replaces(String uniqueId, boolean replicate) {
      this.replacesTopicId = uniqueId;
      this.replicate = replicate;
      return this;
    }
    
    
    /**
     * Submits the API call. Is then handled asynchronously.
     * @return An unique id which specifies the topic.
     * @throws ThresholdException Thrown if low >= high, or if <code>Comparator</code>
     * are incompatible.
     * @throws MissingTopicException Thrown if the topic is replaced is not in the storage.
     */
    String submit() throws ThresholdException, MissingTopicException, MissingParameterException {
      Topic topic;
      if (replicate) replicate();
      if (subscriber == null) throw new MissingParameterException("subscriber");
      if (resource == null) throw new MissingParameterException("resource");
      if (durationSet == false && sendRate < 1) throw new MissingParameterException("sendRate");
      if (groupId == null || machines != null) throw new MissingParameterException("groupId or machines");
      if (eventType == null) throw new MissingParameterException("eventType");
      if (resource == null) throw new MissingParameterException("resource");
      if (groupId != null) {
        Group group = groups.get(groupId);
        topic = TopicFactory.createGroupTopic(storage.generateUniqueId(), resource, eventType, sendRate, group);
      } else {
        topic = TopicFactory.createMachineTopic(storage.generateUniqueId(), resource, eventType, sendRate, machines);
      }
      if (replacesTopicId != null) topic.setReplaces(replacesTopicId, storage);
      if (duration > 0) topic.setDuration(duration);
      if (high != null) topic.setHigh(high);
      if (low != null) topic.setLow(low);
      storage.add(topic);
      sb.registerSubscriber(subscriber, topic);
      storage.notifyBroadcaster();
      return topic.getUniqueId();
    }
    
    private void replicate() throws MissingTopicException {
      Topic relatedTopic = storage.get(replacesTopicId);
      if (relatedTopic instanceof GroupTopic) {
        groupId = (groupId != null) ? groupId : relatedTopic.getGroupId();
      } else {
        machines = (machines != null) ? machines : relatedTopic.getInitialMachines();
      }
      eventType = (eventType != null) ? eventType : relatedTopic.getEventType();
      resource = (resource != null) ? resource : new Resource(relatedTopic.getIndex());
      duration = (durationSet) ? duration : relatedTopic.getDuration();
      sendRate = (sendRateSet) ? sendRate : relatedTopic.getSendRate();
      high = (high != null) ? high : relatedTopic.getHigh();
      low = (low != null) ? low : relatedTopic.getLow();
    }
    
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
