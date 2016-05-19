/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.client.api;

import java.util.Random;
import se.kth.tablespoon.client.topics.Comparator;
import se.kth.tablespoon.client.topics.EventType;
import se.kth.tablespoon.client.topics.ResourceType;
import se.kth.tablespoon.client.topics.Threshold;
import se.kth.tablespoon.client.util.Sleep;

/**
 *
 * @author sebastian
 */
public class DummieEventGenerator implements Runnable {

  private Subscriber subscriber;
  private String groupId;
  private EventType eventType;
  private ResourceType resourceType;
  private int duration;
  private Threshold high;
  private Threshold low;

  private Random rnd = new Random();

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

    return new TablespoonEvent(groupId, machine, value, eventType, resourceType, high, low);
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
    long start = System.currentTimeMillis() / 1000L;
    while (System.currentTimeMillis() / 1000L - start < duration) {
      te = createEvent();
      subscriber.onEventArrival(te);
      Sleep.now(500);
    }
  }

}
