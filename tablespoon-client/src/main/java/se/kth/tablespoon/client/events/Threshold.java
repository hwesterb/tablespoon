package se.kth.tablespoon.client.events;

public class Threshold {
  
  public final double percentage;
  public final Comparator comparator;

  public Threshold(double percentage, Comparator comparator) {
    this.percentage = percentage;
    this.comparator = comparator;
  }
  
  
  

  @Override
  public String toString() {
    return "Threshold{" + "percentage=" + percentage + ", comparator=" + comparator + '}';
  }
  
  
}
