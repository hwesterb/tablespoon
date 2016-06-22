package se.kth.tablespoon.client.api;

import se.kth.tablespoon.client.events.EventType;
import se.kth.tablespoon.client.events.ResourceType;
import se.kth.tablespoon.client.events.Threshold;

public class TablespoonEvent {
  
  private final String groupId;
  private final String machineId;
  private final int collectIndex;
  private final double value;
  private final long timeStamp;
  private final EventType eventType;
  private final ResourceType resourceType;
  private final Threshold high;
  private final Threshold low;
  
  public TablespoonEvent(String groupId, String machineId, int collectIndex, double value, long timeStamp,
      EventType eventType, ResourceType resourceType, Threshold high, Threshold low) {
    this.groupId = groupId;
    this.machineId = machineId;
    this.collectIndex = collectIndex;
    this.value = value;
    this.timeStamp = timeStamp;
    this.eventType = eventType;
    this.resourceType = resourceType;
    this.high = high;
    this.low = low;
  }
  
  public String getGroupId() {
    return groupId;
  }
  
  public String getMachineId() {
    return machineId;
  }
  
  public int getCollectIndex() {
    return collectIndex;
  }
  
  
  public double getValue() {
    return value;
  }
  
  public long getTimeStamp() {
    return timeStamp;
  }
  
  public EventType getEventType() {
    return eventType;
  }
  
  public ResourceType getResourceType() {
    return resourceType;
  }
  
  public Threshold getHigh() {
    return high;
  }
  
  public Threshold getLow() {
    return low;
  }
  
  
  @Override
  public String toString() {
    return "TablespoonEvent{" + "groupId=" + groupId + ", machineId=" + machineId + ", collectIndex=" + collectIndex + ", value=" + value + ", timeStamp=" + timeStamp + ", eventType=" + eventType + ", resourceType=" + resourceType + ", high=" + high + ", low=" + low + '}';
  }
  
  
  
  
}
