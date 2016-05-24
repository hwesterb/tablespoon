package se.kth.tablespoon.agent.metrics;

// This class is placed in an array which describes its collectlIndex. The 

import se.kth.tablespoon.agent.events.ResourceType;

// collectlIndex is referenced in the configfile when defining a new EventDefinition
public class MetricLayout {

  // [CPU]User% describe source (CPU), name (User) and format (%)
  private ResourceType source;
  private String name;
  private MetricFormat format;

  public ResourceType getSource() {
    return source;
  }

  public void setSource(ResourceType source) {
    this.source = source;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public MetricFormat getFormat() {
    return format;
  }

  public void setFormat(MetricFormat format) {
    this.format = format;
  }

}
