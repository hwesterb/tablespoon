/*
* To topicHasChanged this license header, choose License Headers in Project Properties.
* To topicHasChanged this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.topics;

import com.fasterxml.jackson.jr.ob.JSON;
import com.fasterxml.jackson.jr.ob.JSONComposer;
import com.fasterxml.jackson.jr.ob.comp.ObjectComposer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import se.kth.tablespoon.client.main.Groups;

/**
 *
 * @author henke
 */
public abstract class Topic {
  
  private final Lock reentrantLock = new ReentrantLock();
  protected final ArrayList<String> machines;
  private final ArrayList<String> machinesNotified = new ArrayList<>();
  private final int index;
  private final long startTime;
  private double collectionRate = 0.0;
  private double sendRate = 0.0;
  private int duration = 0;
  private final EventType type;
  private Threshold high;
  private Threshold low;
  private final String uniqueId;
  private final String groupId;
  private String json = "";
  private boolean scheduledForRemoval = false;
  
  
    public Topic(int index, long startTime, String uniqueId, EventType type, ArrayList<String> machines, String groupId) {
    this.reentrantLock.lock();
    this.index = index;
    this.startTime = startTime;
    this.uniqueId = uniqueId;
    this.type = type;
    this.machines = machines;
    this.groupId = groupId;
  }
  
  public void lock() {
    this.reentrantLock.lock();
  }
  
  public void unlock() {
    this.reentrantLock.unlock();
  }
  
  public void scheduledForRemoval() throws TopicRemovalException {
    if (duration > 0) throw new TopicRemovalException("The event will expire when duration is over.");
    scheduledForRemoval = true;
  }
  
  // Whenever a topicHasChanged occurs, the machines are no longer notified.
  // Json is no longer valid.
  public void topicHasChanged() {
    machinesNotified.clear();
    json = "";
  }
  
  public ArrayList<String> getMachinesToNotify() {
    ArrayList<String> machinesToNotify = new ArrayList<>();
    for (String machine : machines) {
      if (!machinesNotified.contains(machine)) {
        machinesToNotify.add(machine);
      }
    }
    return machinesToNotify;
  }
  
  
  public abstract void removeDeadMachines(Groups groups);
  
  public boolean hasNoLiveMachines() {
    return machines.isEmpty();
  }
  
  public void addMachine(String machine) {
    machines.add(machine);
  }
  
  public void addToNotifiedMachines(String machine) {
    machinesNotified.add(machine);
  }
  
  public void setSendRate(double sendRate) {
    this.sendRate = sendRate;
  }
  
  public void setCollectionRate(double collectionRate) {
    this.collectionRate = collectionRate;
  }
  
  public void setDuration(int duration) {
    this.duration = duration;
  }
  
  public void setHigh(Threshold high) {
    //changing high removes the low threshold
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
  
  public EventType getType() {
    return type;
  }
  
  public double getCollectionRate() {
    return collectionRate;
  }
  
  public double getSendRate() {
    return sendRate;
  }
  
  public String getUniqueId() {
    return uniqueId;
  }
  
  public boolean isScheduledForRemoval() {
    return scheduledForRemoval;
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
        .put("uniqueId", uniqueId)
        .put("groupId", groupId)
        .put("type", type.toString());
    if (scheduledForRemoval) obj.put("scheduledForRemoval", true);
    if (duration > 0) obj.put("duration", duration);
    if (high!= null) obj.startObjectField("high")
        .put("percentage", high.percentage)
        .put("comparator", high.comparator.toString())
        .end();
    if (low!= null) obj.startObjectField("low")
        .put("percentage", low.percentage)
        .put("comparator", low.comparator.toString())
        .end();
    if (sendRate > 0) obj.put("sendRate", sendRate);
    if (collectionRate > 0) obj.put("collectionRate", collectionRate);
    obj.end();
    json = composer.finish();
  }
  
  public String getJson() {
    return json;
  }
  
  
  
}