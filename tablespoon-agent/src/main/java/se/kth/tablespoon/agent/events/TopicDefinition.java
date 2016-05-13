/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.agent.events;

/**
 *
 * @author henke
 */
public class TopicDefinition {
  
  private int index;
  private int version;
  private long startTime;
  private String uniqueId;
  private String groupId;
  private String type;
  private boolean scheduledForRemoval;
  private boolean hasStarted;
  private int duration;
  private double threshold;
  private Threshold high;
  private Threshold low;
  
  
  public int getCollectlIndex() {
    return index;
  }
  
  public boolean hasDuration() {
    if (duration > 0) return true;
    else return false;
  }
  
  public int getDuration() {
    return duration;
  }
  
  public boolean hasStarted() {
    return hasStarted;
  }
  
  public void setStarted(boolean hasStarted) {
    this.hasStarted = hasStarted;
  }
  
  public long getStartTime() {
    return startTime;
  }
  
  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }
  
  
  
}