package se.kth.tablespoon.agent.metrics;

import se.kth.tablespoon.agent.events.ResourceType;

// This class is placed in an array, index corresponds to collectlIndex.
public class MetricLayout {

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
