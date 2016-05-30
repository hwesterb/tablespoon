package se.kth.tablespoon.agent.events;

import se.kth.tablespoon.agent.file.JsonException;
import com.fasterxml.jackson.jr.ob.JSON;
import com.fasterxml.jackson.jr.ob.impl.DeferredMap;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.agent.metrics.Metric;
import se.kth.tablespoon.agent.util.RuleSupport;
import se.kth.tablespoon.agent.util.Time;


public class Topic {
  
  private int collectIndex;
  private long startTime;
  private final long localStartTime;
  private String uniqueId;
  private String groupId;
  private EventType eventType;
  private int sendRate;
  private int duration;
  private Threshold high;
  private Threshold low;
  private final Queue<Metric> localMetricQueue = new LinkedList<>();
  private final Configuration config = Configuration.getInstance();
  private final static Logger slf4jLogger = LoggerFactory.getLogger(Topic.class);
  private boolean scheduledForRemoval;
  private String replacesTopicId;
  
  public Topic() {
    localStartTime = Time.now();
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
    if (RuleSupport.getNormalizedComparatorType(high.comparator) ==
        Comparator.GREATER_THAN_OR_EQUAL) {
      return high.isValid(value) || low.isValid(value);
    } else {
      return high.isValid(value) && low.isValid(value);
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
    return collectIndex;
  }
  
  public String getUniqueId() {
    return uniqueId;
  }
  
  public String getGroupId() {
    return groupId;
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

  public String getReplacesTopicId() {
    return replacesTopicId;
  }

  public boolean isScheduledForRemoval() {
    return scheduledForRemoval;
  }
  
  public boolean shouldSend(long timeStamp) {
    if (localMetricQueue.size() >= sendRate) {
      long oldestPossibleTime = timeStamp - sendRate;
      while (localMetricQueue.peek().getTimeStamp() < oldestPossibleTime) {
        localMetricQueue.remove();
      }
      if (localMetricQueue.size() == sendRate) return true;
    }
    return false;
  }

  public void interpretJson(String json) throws IOException, JsonException {
    Map<String,Object> map = JSON.std.mapFrom(json);
    if (map.get("collectIndex") == null) throw new JsonException("collectIndex");
    else collectIndex = (int) map.get("collectIndex");   
    if (map.get("startTime") == null) throw new JsonException("startTime");
    else startTime = (int) map.get("startTime");
    if (map.get("uniqueId") == null) throw new JsonException("uniqueId");
    else uniqueId = (String) map.get("uniqueId");
    if (map.get("groupId") == null) throw new JsonException("groupId");
    else groupId = (String) map.get("groupId");
    if (map.get("eventType") == null) throw new JsonException("eventType");
    else eventType = EventType.valueOf((String) map.get("eventType"));
    if (map.get("eventType") == null) throw new JsonException("eventType");
    else eventType = EventType.valueOf((String) map.get("eventType"));
    if (map.get("sendRate") == null) throw new JsonException("sendRate");
    else sendRate = (int) map.get("sendRate");
    if (map.get("replacesTopicId") != null) replacesTopicId = (String) map.get("replacesTopicId");
    if (map.get("duration") != null) duration = (int) map.get("duration");
    if (map.get("high") != null){
      DeferredMap innerMap = (DeferredMap) map.get("high");
      high = new Threshold((double) innerMap.get("percentage"), Comparator.valueOf((String) innerMap.get("comparator")));
    }
    if (map.get("low") != null){
      DeferredMap innerMap = (DeferredMap) map.get("low");
      low = new Threshold((double) innerMap.get("percentage"), Comparator.valueOf((String) innerMap.get("comparator")));
    }
  }
  
  public boolean shouldRemoveJson(String json) throws IOException, JsonException {
    Map<String,Object> map = JSON.std.mapFrom(json);
    if (map.get("collectIndex") == null) throw new JsonException("collectIndex");
    else collectIndex = (int) map.get("collectIndex");
    if (map.get("uniqueId") == null) throw new JsonException("uniqueId");
    else uniqueId = (String) map.get("uniqueId");
    if (map.get("remove") != null) {
      return (boolean) map.get("remove");
    } else {
      return false;
    }
  }
  
}