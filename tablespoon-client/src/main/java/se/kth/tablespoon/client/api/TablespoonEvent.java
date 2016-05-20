/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.api;

import se.kth.tablespoon.client.events.EventType;
import se.kth.tablespoon.client.events.ResourceType;
import se.kth.tablespoon.client.events.Threshold;

public class TablespoonEvent {
  
  private final String groupId;
  private final String machineId;
  private final double value;
  private final EventType eventType;
  private final ResourceType resourceType;
  private final Threshold high;
  private final Threshold low;
  
  public TablespoonEvent(String groupId, String machineId, double value,
      EventType eventType, ResourceType resourceType, Threshold high, Threshold low) {
    this.groupId = groupId;
    this.machineId = machineId;
    this.value = value;
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
  
  public double getValue() {
    return value;
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
  
  
  
}
