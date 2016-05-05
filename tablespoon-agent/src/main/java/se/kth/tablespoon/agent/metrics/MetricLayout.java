package se.kth.tablespoon.agent.metrics;

// This class is placed in an array which describes its collectlIndex. The 
// collectlIndex is referenced in the configfile when defining a new EventDefinition
public class MetricLayout {

  // [CPU]User% describe source (CPU), name (User) and format (%)
  private MetricSource source;
  private String name;
  private MetricFormat format;

  public MetricSource getSource() {
    return source;
  }

  public void setSource(MetricSource source) {
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
