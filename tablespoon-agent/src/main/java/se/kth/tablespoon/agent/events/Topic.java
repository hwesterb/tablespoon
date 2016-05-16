/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.agent.events;

import se.kth.tablespoon.agent.file.TopicJsonException;
import com.fasterxml.jackson.jr.ob.JSON;
import com.fasterxml.jackson.jr.ob.impl.DeferredMap;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import se.kth.tablespoon.agent.metrics.Metric;


public class Topic {
  
  private int index;
  private int version;
  private long startTime;
  private String uniqueId;
  private String groupId;
  private EventType type;
  private Rate minimumRequiredCollectionRate;
  private Rate sendRate;
  private boolean scheduledForRemoval;
  private int duration;
  private Threshold high;
  private Threshold low;
  private long startedOnAgentTime;
  private boolean hasStarted;
  private final Queue<Metric> metricQueue = new LinkedList<>();
  public boolean isNew = true;
  
  
  public Topic() { }
  
  public boolean hasDuration() {
    return duration > 0;
  }

  public void addMetric(Metric metric) {
    metricQueue.add(metric);
  }
  
  public double getAverageOfMeasurements() {
    int counter = 0;
    double metrics = 0.0;
    while (!metricQueue.isEmpty()) {
      Metric metric = metricQueue.poll();
      metrics += metric.getValue();
      counter++;
    }
    return metrics/counter;
  }
  
  public int currentCounterValue() {
    return metricQueue.size();
  }
  
   public boolean hasStarted() {
    return hasStarted;
  }
  
  public int getDuration() {
    return duration;
  }
  
  public long getStartedOnAgentTime() {
    return startedOnAgentTime;
  }
  
  public void setStarted(long startedOnAgentTime) {
    this.startedOnAgentTime = startedOnAgentTime;
    this.hasStarted = true;
  }

  public Rate getMinimumRequiredCollectionRate() {
    return minimumRequiredCollectionRate;
  }

  public Rate getSendRate() {
    return sendRate;
  }
  
  public int getIndex() {
    return index;
  }
  
  public String getUniqueId() {
    return uniqueId;
  }
  
  public int getVersion() {
    return version;
  }

  public Threshold getHigh() {
    return high;
  }

  public Threshold getLow() {
    return low;
  }
  
  public boolean isScheduledForRemoval() {
    return scheduledForRemoval;
  }

  public void interpretJson(String json) throws IOException, TopicJsonException {
    Map<String,Object> map = JSON.std.mapFrom(json);
    
    if (map.get("index") == null) throw new TopicJsonException("index");
    else index = (int) map.get("index");
    
    if (map.get("version") == null) throw new TopicJsonException("version");
    else version = (int) map.get("version");
    
    if (map.get("startTime") == null) throw new TopicJsonException("startTime");
    else startTime = (int) map.get("startTime");
    
    if (map.get("uniqueId") == null) throw new TopicJsonException("uniqueId");
    else uniqueId = (String) map.get("uniqueId");
    
    if (map.get("groupId") == null) throw new TopicJsonException("groupId");
    else groupId = (String) map.get("groupId");
    
    if (map.get("type") == null) throw new TopicJsonException("type");
    else type = EventType.valueOf((String) map.get("type"));
    
    if (map.get("sendRate") == null) throw new TopicJsonException("sendRate");
    else sendRate = Rate.valueOf((String) map.get("sendRate"));
    
    if (map.get("minimumRequiredCollectionRate") != null)  {
      minimumRequiredCollectionRate =  Rate.valueOf((String) map.get("minimumRequiredCollectionRate"));
    }
    else minimumRequiredCollectionRate = sendRate;
    
    if (map.get("duration") != null)  duration = (int) map.get("duration");
    
    if (map.get("scheduledForRemoval") != null) scheduledForRemoval = (boolean) map.get("scheduledForRemoval");
    
    if (map.get("high") != null){
      DeferredMap innerMap = (DeferredMap) map.get("high");
      high = new Threshold((double) innerMap.get("percentage"), Comparator.valueOf((String) innerMap.get("comparator")));
    }
    
    if (map.get("low") != null){
      DeferredMap innerMap = (DeferredMap) map.get("low");
      low = new Threshold((double) innerMap.get("percentage"), Comparator.valueOf((String) innerMap.get("comparator")));
    }
    
  }
  
  
  @Override
  public String toString() {
    return "Topic{" + "index=" + index + ", version=" + version + ", startTime=" + startTime + ", uniqueId=" + uniqueId + ", groupId=" + groupId + ", type=" + type + ", scheduledForRemoval=" + scheduledForRemoval + ", duration=" + duration + ", high=" + high + ", low=" + low + ", startedOnAgentTime=" + startedOnAgentTime + ", hasStarted=" + hasStarted + ", isNew=" + isNew + '}';
  }
  
  
  
  
}