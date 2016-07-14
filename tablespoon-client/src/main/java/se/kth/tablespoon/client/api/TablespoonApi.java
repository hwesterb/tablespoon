package se.kth.tablespoon.client.api;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.client.broadcasting.AgentBroadcasterAssistant;
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

/**
 * Tablespoon API for creating, replacing, replicating and removing topics. Topics defines events which are system
 * measurements. Events are sent to event stream processor and filtered according to the content of the topic.
 */
public class TablespoonApi {

  private TopicStorage storage;
  private Groups groups;
  private SubscriberBroadcaster sb;
  private final static Logger slf4jLogger = LoggerFactory.getLogger(TablespoonApi.class);


  public TablespoonApi(TopicStorage storage, Groups groups, SubscriberBroadcaster sb) {
    this.storage = storage;
    this.groups = groups;
    this.sb = sb;
  }

  public Submitter submitter() {
    return new Submitter();
  }

  public class Submitter {

    private Subscriber subscriber;
    private String groupId;
    private EventType eventType;
    private Resource resource;
    private boolean durationSet = false;
    private int duration;
    private boolean sendRateSet = false;
    private int retrievalDelay;
    private boolean retrievalDelaySet = false;
    private int sendRate;
    private Set<String> machines;
    private Threshold high;
    private Threshold low;
    private String replacesTopicId;
    private boolean replicate;

    /**
     * This parameter is mandatory.
     *
     * @param subscriber A <code>Subscriber</code> which receives <code>TablespoonEvent</code>.
     * @return <code>Submitter</code> for <code>TablespoonAPI</code>.
     */
    public Submitter subscriber(Subscriber subscriber) {
      this.subscriber = subscriber;
      return this;
    }

    /**
     * This parameter is mandatory if not replicated.
     *
     * @param eventType Specifies how to gather and filter information.
     * @return <code>Submitter</code> for <code>TablespoonAPI</code>.
     */
    public Submitter eventType(EventType eventType) {
      this.eventType = eventType;
      return this;
    }

    /**
     * This parameter is mandatory if not replicated.
     *
     * @param resource Type of resource which should be collected.
     * @return <code>Submitter</code> for <code>TablespoonAPI</code>.
     */
    public Submitter resource(Resource resource) {
      this.resource = resource;
      return this;
    }

    /**
     * This parameter is mandatory if not replicated. Lowest possible send rate is 1.
     *
     * @param sendRate The rate at which the agent sends messages.
     * @return <code>Submitter</code> for <code>TablespoonAPI</code>.
     */
    public Submitter sendRate(int sendRate) {
      sendRateSet = true;
      this.sendRate = sendRate;
      return this;
    }

    /**
     * This parameter is not mandatory. If this parameter is not set the topic will be active until removed.
     *
     * @param duration Duration of a topic. Will expire automatically after duration.
     * @return <code>Submitter</code> for <code>TablespoonAPI</code>.
     */
    public Submitter duration(int duration) {
      durationSet = true;
      this.duration = duration;
      return this;
    }

    /**
     * This parameter is not mandatory. If this parameter is not set, the retrieval delay will be set to 50% of the send rate.
     * 
     * @param retrievalDelay An artifical delay added to prevent excessive querying
     * when it is not necessary. If 0 is set the client will query whenever
     * a thread is available. The unit is milliseconds.
     * @return <code>Submitter</code> for <code>TablespoonAPI</code>.
     */
    public Submitter retrivalDelay(int retrievalDelay) {
      this.retrievalDelay = retrievalDelay;
      retrievalDelaySet = true;
      return this;
    }
    
    /**
     * This parameter is mandatory if not replicated. Use either this or {@link #machines(Set<String>) getMachines}.
     * 
     * @param groupId Id which specifies a group.
     * @return <code>Submitter</code> for <code>TablespoonAPI</code>.
     */
    public Submitter groupId(String groupId) {
      this.machines = null;
      this.groupId = groupId;
      return this;
    }

    /**
     * This parameter is not mandatory if not replicated. Use either this or {@link #groupId(String) groupId}.
     *
     * @param machines The getMachines where the topic should be active.
     * @return <code>Submitter</code> for <code>TablespoonAPI</code>.
     */
    public Submitter machines(Set<String> machines) {
      this.groupId = null;
      this.machines = machines;
      return this;
    }

    /**
     * This parameter is not mandatory.
     *
     * @param high A <code>Threshold</code> that determines the percentage or percentile when filtering events.
     * @return <code>Submitter</code> for <code>TablespoonAPI</code>.
     */
    public Submitter high(Threshold high) {
      this.high = high;
      return this;
    }

    /**
     * This parameter is not mandatory.
     *
     * @param low A <code>Threshold</code> that determines the percentage or percentile when filtering events.
     * @return <code>Submitter</code> for <code>TablespoonAPI</code>.
     */
    public Submitter low(Threshold low) {
      this.low = low;
      return this;
    }

    public Submitter replaces(String uniqueId, boolean replicate) {
      this.replacesTopicId = uniqueId;
      this.replicate = replicate;
      return this;
    }

    /**
     * Submits the API call. The call is then handled asynchronously.
     *
     * @return An unique id which specifies the topic.
     * @throws ThresholdException Thrown if low >= high, or if <code>Comparator</code> are incompatible.
     * @throws MissingTopicException Thrown if the topic that is being replaced is not present. It might have been
     * removed due to its duration being overdue, or that no machines were active on that topic.
     * @throws MissingParameterException Thrown if mandatory parameter is not specified.
     * @throws IOException If json could not be generated.
     */
    public String submit() throws ThresholdException, MissingTopicException, MissingParameterException, IOException {
      Topic topic;
      if (replicate) {
        replicate();
      }
      if (subscriber == null) {
        throw new MissingParameterException("subscriber");
      }
      if (resource == null) {
        throw new MissingParameterException("resource");
      }
      if (durationSet == false && sendRate < 1) {
        throw new MissingParameterException("sendRate");
      }
      if (groupId == null || machines != null) {
        throw new MissingParameterException("groupId or machines");
      }
      if (eventType == null) {
        throw new MissingParameterException("eventType");
      }
      if (resource == null) {
        throw new MissingParameterException("resource");
      }
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
      if (retrievalDelaySet) topic.setRetrievalDelay(retrievalDelay);
      else {
        topic.setRetrievalDelay((sendRate * 1000) / 2);
      }
      storage.add(topic);
      sb.registerSubscriber(subscriber, topic);
      storage.notifyBroadcaster();
      slf4jLogger.info("Topic " + topic.getUniqueId() + " was successfully submitted in API. ");
      return topic.getUniqueId();
    }

    private void replicate() throws MissingTopicException {
      Topic relatedTopic = storage.get(replacesTopicId);
      if (relatedTopic instanceof GroupTopic) {
        groupId = (groupId != null) ? groupId : relatedTopic.getGroupId();
      } else {
        machines = (machines != null) ? machines : relatedTopic.getMachines();
      }
      eventType = (eventType != null) ? eventType : relatedTopic.getEventType();
      resource = (resource != null) ? resource : new Resource(relatedTopic.getCollectIndex());
      duration = (durationSet) ? duration : relatedTopic.getDuration();
      sendRate = (sendRateSet) ? sendRate : relatedTopic.getSendRate();
      retrievalDelay = (retrievalDelaySet) ? retrievalDelay : relatedTopic.getRetrievalDelay();
      high = (high != null) ? high : relatedTopic.getHigh();
      low = (low != null) ? low : relatedTopic.getLow();
    }

  }

  /**
   * This call removes a topic.
   *
   * @param uniqueId An unique id which specifies the topic.
   * @throws MissingTopicException Thrown if the topic is not present in the storage.
   */
  public void removeTopic(String uniqueId) throws MissingTopicException {
    storage.remove(uniqueId);
    storage.notifyBroadcaster();
  }

}
