package se.kth.tablespoon.client.api;

import java.util.Random;
import se.kth.tablespoon.client.events.Comparator;
import se.kth.tablespoon.client.events.EventType;
import se.kth.tablespoon.client.events.ResourceType;
import se.kth.tablespoon.client.events.Threshold;
import se.kth.tablespoon.client.util.Time;

public class DummieEventGenerator implements Runnable {

  private final Subscriber subscriber;
  private final String groupId;
  private final EventType eventType;
  private final ResourceType resourceType;
  private final int duration;
  private final Threshold high;
  private final Threshold low;

  private final Random rnd = new Random();

  public DummieEventGenerator(Subscriber subscriber, String groupId, EventType eventType,
      ResourceType resourceType, int duration, Threshold high, Threshold low) {
    this.subscriber = subscriber;
    this.groupId = groupId;
    this.eventType = eventType;
    this.resourceType = resourceType;
    this.duration = duration;
    this.high = high;
    this.low = low;
  }

  public TablespoonEvent createEvent() {
    int value = rnd.nextInt(100);
    if (high != null) {
      if (low != null) {
        value = twoThresholds();
      } else {
        value = oneThreshold();
      }
    }
    String machine = "machine" + rnd.nextInt(10);

    return new TablespoonEvent(groupId, machine, value, Time.now(), eventType, resourceType, high, low);
  }

  private int oneThreshold() {
    if (getNormalizedComparatorType(high.comparator)
        == Comparator.GREATER_THAN_OR_EQUAL) {
      return rnd.nextInt((int) (100 - high.percentage)) + (int) high.percentage;
    } else {
      return rnd.nextInt((int) high.percentage);
    }
  }

  private int twoThresholds() {
    if (getNormalizedComparatorType(high.comparator)
        == Comparator.GREATER_THAN_OR_EQUAL) {
      if (rnd.nextBoolean()) {
        // somewhere between high.percentage and 100
        return rnd.nextInt((int) (100 - high.percentage)) + (int) high.percentage;
      } else {
        return rnd.nextInt((int) low.percentage);
      }
    } else {
      // somewhere between low.percentage and high.percentage
      return rnd.nextInt((int) (high.percentage + low.percentage)) - (int) low.percentage;
    }
  }

  private Comparator getNormalizedComparatorType(Comparator comparator) {
    if (comparator.equals(Comparator.GREATER_THAN) || comparator.equals(Comparator.GREATER_THAN_OR_EQUAL)) {
      return Comparator.GREATER_THAN_OR_EQUAL;
    } else {
      return Comparator.LESS_THAN_OR_EQUAL;
    }
  }

  @Override
  public void run() {
    TablespoonEvent te;
    long start = Time.now();
    while (Time.now() - start < duration) {
      te = createEvent();
      subscriber.onEventArrival(te);
      Time.sleep(500);
    }
  }

}
