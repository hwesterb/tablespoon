/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.agent.events;

import se.kth.tablespoon.agent.file.JsonException;
import com.fasterxml.jackson.jr.ob.JSON;
import com.fasterxml.jackson.jr.ob.impl.DeferredMap;
import com.oracle.jrockit.jfr.ContentType;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.agent.metrics.Metric;


public class Topic {
  
  private int index;
  private int version;
  private long startTime;
  private final long localStartTime;
  private String uniqueId;
  private String groupId;
  private EventType type;
  private int sendRate;
  private boolean scheduledForRemoval;
  private int duration;
  private Threshold high;
  private Threshold low;
  private final Queue<Metric> localMetricQueue = new LinkedList<>();
  private final Configuration config = Configuration.getInstance();
  private final static Logger slf4jLogger = LoggerFactory.getLogger(Topic.class);
  
  public Topic() {
    localStartTime = System.currentTimeMillis() / 1000L;
  }
  
  public boolean hasDuration() {
    return duration > 0;
  }
  
  public void addToLocal(Metric metric) {
    localMetricQueue.add(metric);
  }
  
  public double getAverageOfMeasurements() {
    int counter = 0;
    double metrics = 0.0;
    while (!localMetricQueue.isEmpty()) {
      Metric metric = localMetricQueue.poll();
      metrics += metric.getValue();
      counter++;
    }
    return metrics/counter;
  }
  
  public boolean isValid(double value) {
    if (high != null && low != null) {
      return twoThresholds(value);
    } else if (high != null) {
      return high.isValid(value);
    }
    return true;
  }
  
  private boolean twoThresholds(double value) {
    if (getNormalizedComparatorType(high.comparator) ==
        Comparator.GREATER_THAN_OR_EQUAL) {
      return high.isValid(value) || low.isValid(value);
    } else {
      return high.isValid(value) && low.isValid(value);
    }
  }
  
  private Comparator getNormalizedComparatorType(Comparator comparator) {
    if (comparator.equals(Comparator.GREATER_THAN) || comparator.equals(Comparator.GREATER_THAN_OR_EQUAL)) {
      return Comparator.GREATER_THAN_OR_EQUAL;
    } else {
      return Comparator.LESS_THAN_OR_EQUAL;
    }
  }

  public long getStartTime() {
    return startTime;
  }

  public long getLocalStartTime() {
    return localStartTime;
  }
  
  public int getDuration() {
    return duration;
  }
  
  public int getSendRate() {
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
  
  public boolean shouldSend() {
    if (localMetricQueue.size() >= sendRate) {
      long oldestPossibleTime = localMetricQueue.element().getTimeStamp() - sendRate;
      while (localMetricQueue.peek().getTimeStamp() < oldestPossibleTime) {
        localMetricQueue.remove();
      }
      if (localMetricQueue.size() == sendRate) return true;
    }
    return false;
  }
  
  
  public void interpretJson(String json) throws IOException, JsonException {
    Map<String,Object> map = JSON.std.mapFrom(json);
    
    if (map.get("index") == null) throw new JsonException("index");
    else index = (int) map.get("index");
    
    if (map.get("version") == null) throw new JsonException("version");
    else version = (int) map.get("version");
    
    if (map.get("startTime") == null) throw new JsonException("startTime");
    else startTime = (int) map.get("startTime");
    
    if (map.get("uniqueId") == null) throw new JsonException("uniqueId");
    else uniqueId = (String) map.get("uniqueId");
    
    if (map.get("groupId") == null) throw new JsonException("groupId");
    else groupId = (String) map.get("groupId");
    
    if (map.get("type") == null) throw new JsonException("type");
    else type = EventType.valueOf((String) map.get("type"));
    
    if (map.get("sendRate") == null) throw new JsonException("sendRate");
    else sendRate = (int) map.get("sendRate");
    
    if (map.get("duration") != null) duration = (int) map.get("duration");
    
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
  
  
  
  
  
  
}