package se.kth.tablespoon.agent.metrics;

import se.kth.tablespoon.agent.events.ResourceType;

public class Metric {
  
  private final int collectIndex;
  private final ResourceType resourceType;
  private final MetricFormat format;
  private final long timeStamp;
  private final String name;
  private final double value;

  public Metric(int collectIndex, ResourceType resourceType, MetricFormat format,
      long timeStamp, String name, double value) {
    this.collectIndex = collectIndex;
    this.resourceType = resourceType;
    this.format = format;
    this.timeStamp = timeStamp;
    this.name = name;
    this.value = value;
  }

  public int getCollectIndex() {
    return collectIndex;
  }

  public ResourceType getResourceType() {
    return resourceType;
  }

  public MetricFormat getFormat() {
    return format;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public String getName() {
    return name;
  }

  public double getValue() {
    return value;
  }
  
  
  
  
}
