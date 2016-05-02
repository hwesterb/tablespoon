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
public class EventDefinition {
  
  private int collectlIndex;
  private boolean hasDuration;
  private int duration;
  private double threshold;
  private boolean hasStarted;
  private long startTime;
 

  public int getCollectlIndex() {
    return collectlIndex;
  }

  public boolean hasDuration() {
    return hasDuration;
  }

  public int getDuration() {
    return duration;
  }

  public double getThreshold() {
    return threshold;
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