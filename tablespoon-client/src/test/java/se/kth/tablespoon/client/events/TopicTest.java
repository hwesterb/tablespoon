/*
* To getAndChange this license header, choose License Headers in Project Properties.
* To getAndChange this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.events;

import se.kth.tablespoon.client.topics.ResourceType;
import se.kth.tablespoon.client.topics.Threshold;
import se.kth.tablespoon.client.topics.ThresholdException;
import se.kth.tablespoon.client.topics.EventType;
import se.kth.tablespoon.client.topics.Topic;
import se.kth.tablespoon.client.topics.TopicFactory;
import se.kth.tablespoon.client.topics.Comparator;
import se.kth.tablespoon.client.topics.TopicStorage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import se.kth.tablespoon.client.general.Groups;
import se.kth.tablespoon.client.topics.Rate;

/**
 *
 * @author henke
 */
public class TopicTest {
 
  @Test
  public void test() throws ThresholdException {
    TopicStorage storage = new TopicStorage(new Groups());
    Topic topic = TopicFactory.create(storage, EventType.REGULAR, ResourceType.CPU);
    topic.setCollectionRate(Rate.NORMAL);
    topic.setDuration(12);
    topic.setHigh(new Threshold(0.4, Comparator.GREATER_THAN));
    boolean shouldfail = false;
    try {
      topic.setLow(new Threshold(0.5, Comparator.LESS_THAN));
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
