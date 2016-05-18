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
public class Topics {
  
  private final Configuration config = Configuration.getInstance();
  private final TreeMap<String, Topic> topics = new TreeMap<>();
  
  public void addTopic(Topic topic) {
    topics.put(topic.getUniqueId(), topic);
  }
  
  public Topic findTopic(String uniqueId) {
    return topics.get(uniqueId);
  }
  
  public ArrayList<RiemannEvent> extractRiemannEvents(Metric metric, ArrayList<Topic> relevantTopics) {
    expireOldTopics(metric);
    ArrayList<RiemannEvent> riemannEvents = new ArrayList<>();
    for (Topic topic : relevantTopics) {
      if (topic.metricQueueIsEmpty() == false) topic.expireOldMetrics();
      addRiemannEventIfReady(riemannEvents, metric, topic);
    }
    return riemannEvents;
  }
  
  private void addRiemannEventIfReady(ArrayList<RiemannEvent> riemannEvents, Metric metric, Topic topic) {
    int sendWhenCounterIs = RaterInterpreter.sendWhenCounterIs(topic.getSendRate(),
        config.getCollectlCollectionRate());
    if (topic.currentCounterValue() == sendWhenCounterIs) {
      addRiemannEvent(riemannEvents, metric, topic);
    } else if (topic.currentCounterValue() > sendWhenCounterIs) {
      // The cause for this may be a change in the sending or collection rates.
      // The principle here is to send what you have rather than discarding information.
      addRiemannEvent(riemannEvents, metric, topic);
    } else {
      topic.addMetric(metric);
    }
  }
  
  private void addRiemannEvent(ArrayList<RiemannEvent> riemannEvents, Metric metric, Topic topic) {
    double value = topic.getAverageOfMeasurements();
    if (topic.isValid(value)) {
      riemannEvents.add(createRiemannEvent(metric, value, topic.getUniqueId()));
    }
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
  
  private void expireOldTopics(Metric metric) {
    Iterator<Topic> iterator = topics.values().iterator();
    while (iterator.hasNext()) {
      Topic topic = iterator.next();
      if (durationHasEnded(metric, topic) || topic.isScheduledForRemoval()) {
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
  
  public ArrayList<Topic> getRelevantTopicsBeloningToIndex(int index) {
    ArrayList<Topic> relevantTopics = new ArrayList<>();
    for (Topic topic : topics.values()) {
      if (topic.getIndex() == index) relevantTopics.add(topic);
    }
    return relevantTopics;
  }
  
  
}
