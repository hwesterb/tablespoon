/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.agent.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import se.kth.tablespoon.agent.metrics.Metric;

/**
 *
 * @author henke
 */
public class RelatedTopics {
  
  private final Configuration config = Configuration.getInstance();
  private final TreeMap<String, Topic> topics = new TreeMap<>();
  
  public void addTopic(Topic topic) {
    topics.put(topic.getUniqueId(), topic);
  }
  
  public Topic contains(String uniqueId) {
    for (Topic topic : topics.values()) {
      if (topic.getUniqueId() == null ? uniqueId == null : topic.getUniqueId().equals(uniqueId)) return topic;
    }
    return null;
  }
  
  public ArrayList<RiemannEvent> extractRiemannEvents(Metric metric) {
    expireIfOld(metric);
    ArrayList<RiemannEvent> riemannEvents = new ArrayList<>();
    for (Topic topic : topics.values()) {
      addRiemannEvent(riemannEvents, metric, topic);
    }
    return riemannEvents;
  }
  
  private void addRiemannEventIfReady(Topic topic, Metric metric, ArrayList<RiemannEvent> riemannEvents) {
    int sendWhenCounterIs = RaterInterpreter.sendWhenCounterIs(topic.getSendRate(),
        config.getCollectlCollectionRate());
    if (sendWhenCounterIs == topic.currentCounterValue()) {
      addRiemannEvent(riemannEvents, metric, topic);
    } else {
      topic.addMetric(metric);
    }
  }
  
  private void addRiemannEvent(ArrayList<RiemannEvent> riemannEvents, Metric metric, Topic topic) {
    double value = topic.getAverageOfMeasurements();
    for (RiemannEvent event : riemannEvents) {
      if (addToAnotherEvent(event, topic.getUniqueId(), value)) return;
    }
    riemannEvents.add(createRiemannEvent(metric, value, topic.getUniqueId()));
  }
  
  private boolean addToAnotherEvent(RiemannEvent event, String uniqueId, double value) {
    if (event.getValue() == value) {
      event.addTag(uniqueId);
      return true;
    }
    else return false;
  }
  
  private RiemannEvent createRiemannEvent(Metric metric, double value, String uniqueId) {
    RiemannEvent riemannEvent = new RiemannEvent(metric.getSource().toString(),
        null,
        metric.getName(),
        value,
        metric.getTimeStamp(),
        config.getRiemannEventTtl());
    riemannEvent.addTag(uniqueId);
    return riemannEvent;
  }
  
  private void expireIfOld(Metric metric) {
    Iterator<Topic> iterator = topics.values().iterator();
    while (iterator.hasNext()) {
      Topic topic = iterator.next();
      if (durationHasEnded(metric, topic)) {
        topics.put(topic.getUniqueId(), null);
      }
    }
  }
  
  private boolean durationHasEnded(Metric metric, Topic topic) {
    if (topic.hasDuration()) {
      if (topic.hasStarted()) {
        long now = System.currentTimeMillis() / 1000L;
        if ((now - metric.getTimeStamp()) > topic.getDuration()) {
          return true;
        }
      } else {
        topic.setStarted(metric.getTimeStamp());
      }
    }
    return false;
  }
  
  
  
  
}
