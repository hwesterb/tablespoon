/*
* To topicHasChanged this license header, choose License Headers in Project Properties.
* To topicHasChanged this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.topics;

import se.kth.tablespoon.client.events.Threshold;
import se.kth.tablespoon.client.events.Comparator;
import se.kth.tablespoon.client.events.EventType;
import com.fasterxml.jackson.jr.ob.JSON;
import com.fasterxml.jackson.jr.ob.JSONComposer;
import com.fasterxml.jackson.jr.ob.comp.ObjectComposer;
import java.io.IOException;
import java.util.HashSet;
import se.kth.tablespoon.client.general.Groups;


public abstract class Topic {
  
  protected final HashSet<String> machinesNotified = new HashSet<>();
  private final int index;
  private final long startTime;
  private int sendRate;
  private int duration = 0;
  private final EventType eventType;
  private Threshold high;
  private Threshold low;
  private final String uniqueId;
  private String replacesTopicId;
  protected final String groupId;
  private String json = "";
  private boolean scheduledForRemoval = false;
  
  
  public Topic(int index, long startTime, String uniqueId, EventType type, int sendRate, String groupId) {
    this.index = index;
    this.startTime = startTime;
    this.uniqueId = uniqueId;
    this.eventType = type;
    this.sendRate = sendRate;
    this.groupId = groupId;
  }
  
  
  public void scheduledForRemoval() throws TopicRemovalException {
    if (duration > 0) throw new TopicRemovalException("The event will expire when duration is over.");
    scheduledForRemoval = true;
    machinesNotified.clear();
  }
  
  
  public abstract HashSet<String> getMachinesToNotify();
  
  public abstract void updateMachineState(Groups groups);
  
  public abstract boolean hasNoLiveMachines();
  
  public abstract HashSet<String> getInitialMachines();
  
  public void addToNotifiedMachines(HashSet<String> machines) {
    machinesNotified.addAll(machines);
  }
  
  
  public void setSendRate(int sendRate) {
    this.sendRate = sendRate;
  }
  
  public void setDuration(int duration) {
    this.duration = duration;
  }
  
  public void setHigh(Threshold high) {
    this.low = null;
    this.high = high;
  }
  
  public void setLow(Threshold low) throws ThresholdException {
    if (high==null ||
        getNormalizedComparatorType(high.comparator) == getNormalizedComparatorType(low.comparator) ||
        high.percentage <= low.percentage) {
      throw new ThresholdException();
    }
    this.low = low;
  }
  
  public int getIndex() {
    return index;
  }
  
  public long getStartTime() {
    return startTime;
  }
  
  public int getDuration() {
    return duration;
  }
  
  public Threshold getHigh() {
    return high;
  }
  
  public Threshold getLow() {
    return low;
  }
  
  public EventType getEventType() {
    return eventType;
  }
  
  public int getSendRate() {
    return sendRate;
  }
  
  public String getUniqueId() {
    return uniqueId;
  }
  
  public boolean isScheduledForRemoval() {
    return scheduledForRemoval;
  }
  
  public String getGroupId() {
    return groupId;
  }
 
  private Comparator getNormalizedComparatorType(Comparator comparator) {
    if (comparator.equals(Comparator.GREATER_THAN) || comparator.equals(Comparator.GREATER_THAN_OR_EQUAL)) {
      return Comparator.GREATER_THAN_OR_EQUAL;
    } else {
      return Comparator.LESS_THAN_OR_EQUAL;
    }
  }
  
  public void generateJson() throws IOException {
    JSONComposer<String> composer = JSON.std
        .with(JSON.Feature.PRETTY_PRINT_OUTPUT)
        .composeString();
    ObjectComposer obj = composer.startObject();
    obj.put("index", index)
        .put("startTime", startTime)
        .put("uniqueId", uniqueId)
        .put("groupId", groupId)
        .put("eventType", eventType.toString())
        .put("sendRate", sendRate);
    if (scheduledForRemoval) {
      obj.put("scheduledForRemoval", true);
    } else {
      if (duration > 0) obj.put("duration", duration);
      if (high!= null) obj.startObjectField("high")
          .put("percentage", high.percentage)
          .put("comparator", high.comparator.toString())
          .end();
      if (low!= null) obj.startObjectField("low")
          .put("percentage", low.percentage)
          .put("comparator", low.comparator.toString())
          .end();
    }
    obj.end();
    json = composer.finish();
  }
  
  public String getJson() {
    return json;
  }

  public void setReplaces(String replacesTopicId, TopicStorage storage) throws MissingTopicException {
    if (storage.uniqueIdExists(replacesTopicId) == false) throw new MissingTopicException();
    this.replacesTopicId = replacesTopicId;
  }
  
  
  
}