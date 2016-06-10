package se.kth.tablespoon.agent.events;

import se.kth.tablespoon.agent.file.ReplacingTopicException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.agent.metrics.Metric;

public class Topics {
  
  private final Configuration config = Configuration.getInstance();
  private final ConcurrentHashMap<String, Topic> topics = new  ConcurrentHashMap<>();
  private final static Logger slf4jLogger = LoggerFactory.getLogger(Topics.class);
  
  public void addTopic(Topic topic) {
    topics.put(topic.getUniqueId(), topic);
  }
  
  public Topic findTopic(String uniqueId) {
    return topics.get(uniqueId);
  }
  
  public ArrayList<RiemannEvent> extractRiemannEvents(Metric metric, ArrayList<Topic> relevantTopics) {
    ArrayList<RiemannEvent> riemannEvents = new ArrayList<>();
    for (Topic topic : relevantTopics) {
      topic.addToLocal(metric);
      if (topic.shouldSend(metric.getTimeStamp())) {
        addRiemannEvent(metric, riemannEvents, topic);
      }
    }
    return riemannEvents;
  }
  
  private void addRiemannEvent(Metric metric, ArrayList<RiemannEvent> riemannEvents, Topic topic) {
    double value = topic.getAverageOfMeasurements();
    if (topic.isValid(value)) {
      riemannEvents.add(createRiemannEvent(metric, topic, value, topic.getUniqueId()));
    }
  }
  
  private RiemannEvent createRiemannEvent(Metric metric, Topic topic, double value, String uniqueId) {
    RiemannEvent riemannEvent = new RiemannEvent(uniqueId,
        "tablespoon",
        metric.getName() + " in " + metric.getFormat().toString().toLowerCase()  + ".",
        value,
        metric.getTimeStamp(),
        config.getRiemannEventTtl());
    riemannEvent.addTag(metric.getResourceType().toString());
    riemannEvent.addTag(topic.getEventType().toString());
    return riemannEvent;
  }
  
  public void clean(Metric metric, ArrayList<Topic> relevant) {
    Iterator<Topic> iterator = topics.values().iterator();
    while (iterator.hasNext()) {
      Topic topic = iterator.next();
      if (durationHasEnded(metric.getTimeStamp(), topic) || topic.isScheduledForRemoval()) {
        slf4jLogger.info("Removing topic " + topic.getUniqueId() + " from memory.");
        topics.remove(topic.getUniqueId());
      }
    }
  }
  
  public void remove(Topic topic) {
    slf4jLogger.info("Removing topic " + topic.getUniqueId() + " from memory.");
    topics.put(topic.getUniqueId(), null);
  }
  
  public void replace(String topicId, String replacesTopicId) throws ReplacingTopicException {
    Topic replacesTopic = topics.get(replacesTopicId);
    if (replacesTopic == null) throw new ReplacingTopicException(replacesTopicId);
    slf4jLogger.info(topicId + " replaces " + replacesTopicId + ".");
    remove(replacesTopic);
  }
  
  
  private boolean durationHasEnded(long timeStamp, Topic topic) {
    if (topic.hasDuration()) {
      if ((timeStamp - topic.getLocalStartTime()) > topic.getDuration()) {
        return true;
      }
    }
    return false;
  }
  
  public ArrayList<Topic> getRelevantTopicsBeloningToIndex(int index) {
    ArrayList<Topic> relevantTopics = new ArrayList<>();
    for (Topic topic : topics.values()) {
      if (topic.getIndex() == index) relevantTopics.add(topic);
    }
    return relevantTopics;
  }
  
  
  
  
}
