package se.kth.tablespoon.agent.events;

public class Threshold {
  
  public final double percentage;
  public final Comparator comparator;
  
  public Threshold(double percentage, Comparator comparator) {
    this.percentage = percentage;
    this.comparator = comparator;
  }
  
  public boolean isValid(double value) {
    switch (comparator) {
      case GREATER_THAN:
        if (value > percentage) return true;
        break;
      case GREATER_THAN_OR_EQUAL:
        if (value >= percentage) return true;
        break;
      case LESS_THAN:
        if (value < percentage) return true;
        break;
      case LESS_THAN_OR_EQUAL:
        if (value <= percentage) return true;
        break;
    }
    return false;
  }
  
  @Override
  public String toString() {
    return "Threshold{" + "percentage=" + percentage + ", comparator=" + comparator + '}';
  }
  
  
}
