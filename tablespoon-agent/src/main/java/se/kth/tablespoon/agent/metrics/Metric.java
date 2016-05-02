package se.kth.tablespoon.agent.metrics;

public class Metric {
  
  private int collectIndex;
  private MetricSource source;
  private MetricFormat format;
  private long timeStamp;
  private String name;
  private double value;

  public Metric(int collectIndex, MetricSource source, MetricFormat format, long timeStamp, String name, double value) {
    this.collectIndex = collectIndex;
    this.source = source;
    this.format = format;
    this.timeStamp = timeStamp;
    this.name = name;
    this.value = value;
  }

  public int getCollectIndex() {
    return collectIndex;
  }

  public MetricSource getSource() {
    return source;
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
