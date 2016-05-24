/*
 * To getAndChange this license header, choose License Headers in Project Properties.
 * To getAndChange this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.client.topics;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import se.kth.tablespoon.client.events.Comparator;
import se.kth.tablespoon.client.events.EventType;
import se.kth.tablespoon.client.events.Resource;
import se.kth.tablespoon.client.events.ResourceType;
import se.kth.tablespoon.client.events.Threshold;
import static org.junit.Assert.*;
import se.kth.tablespoon.client.general.Groups;

/**
 *
 * @author henke
 */
public class TopicTest {

  @Test
  public void test() throws ThresholdException {
    TopicStorage storage = new TopicStorage(new Groups());
    Resource resource = new Resource(ResourceType.CPU);
    Topic topic = TopicFactory.create(storage, resource, EventType.REGULAR, 1, null);
    topic.setDuration(12);
    topic.setHigh(new Threshold(40.0, Comparator.GREATER_THAN));
    boolean shouldfail = false;
    try {
      topic.setLow(new Threshold(50.0, Comparator.LESS_THAN));
    } catch (ThresholdException ex) {
      shouldfail = true;
    }
    assertTrue(shouldfail);
    topic.setLow(new Threshold(0.1, Comparator.LESS_THAN));

    try {
      topic.generateJson();
    } catch (IOException ex) {
      Logger.getLogger(TopicTest.class.getName()).log(Level.SEVERE, null, ex);
    }
    System.out.println(topic.getJson());

  }
}
