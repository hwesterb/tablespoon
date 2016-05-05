/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.events;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author henke
 */
public class EventDefinition {
  
  private final ArrayList<String> activated = new ArrayList<>();
  private final ArrayList<String> notified = new ArrayList<>();
  private final int index;
  private final long startTime;
  private int collectionRate = 0;
  private double sendRate = 0.0;
  private int duration;
  private Threshold high;
  private Threshold low;
  private final String uniqueId;
  
  public EventDefinition(int index, long startTime, String uniqueId) {
    this.index = index;
    this.startTime = startTime;
    this.uniqueId = uniqueId;
  }
  
  public void removeDeadMachines(ArrayList<String> liveMachines) {
    for (String machine : liveMachines) {
      Iterator<String> iterator = activated.iterator();
      while (iterator.hasNext()) {
        String activatedMachine = iterator.next();
        if (!activatedMachine.contentEquals(machine)) iterator.remove();
      }     
    }
  }
  
  public boolean hasNoLiveMachines() {
    return activated.isEmpty();
  }
  
  public void addMachine(String machine) {
    activated.add(machine);
  }

  public int getCollectionRate() {
    return collectionRate;
  }

  public void setCollectionRate(int collectionRate) {
    this.collectionRate = collectionRate;
  }

  public double getSendRate() {
    return sendRate;
  }

  public void setSendRate(double sendRate) {
    this.sendRate = sendRate;
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
  
  public void setDuration(int duration) {
    this.duration = duration;
  }
  
  public Threshold getHigh() {
    return high;
  }
  
  public void setHigh(Threshold high) {
    //changing high removes the low threshold
    this.low = null;
    this.high = high;
  }
  
  public Threshold getLow() {
    return low;
  }
  
  public String getUniqueId() {
    return uniqueId;
  }
  
  public void setLow(Threshold low) throws ThresholdException {
    if (high==null ||
        getNormalizedComparatorType(high.comparator) == getNormalizedComparatorType(low.comparator) ||
        high.percentage < low.percentage) {
      throw new ThresholdException();
    }
    this.low = low;
  }
  
  private Comparator getNormalizedComparatorType(Comparator comparator) {
    if (comparator.equals(Comparator.GREATER_THAN) || comparator.equals(Comparator.GREATER_THAN_OR_EQUAL)) {
      return Comparator.GREATER_THAN_OR_EQUAL;
    } else {
      return Comparator.LESS_THAN_OR_EQUAL;
    }
  }
  
}