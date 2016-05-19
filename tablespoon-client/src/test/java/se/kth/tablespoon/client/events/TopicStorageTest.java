/*
* To getAndChange this license header, choose License Headers in Project Properties.
* To getAndChange this template file, choose Tools | Templates
* and open the template in the topicitor.
*/
package se.kth.tablespoon.client.events;

import se.kth.tablespoon.client.topics.EventType;
import se.kth.tablespoon.client.topics.Topic;
import se.kth.tablespoon.client.topics.TopicFactory;
import se.kth.tablespoon.client.topics.TopicStorage;
import org.junit.Test;
import static org.junit.Assert.*;
import se.kth.tablespoon.client.general.Group;
import se.kth.tablespoon.client.general.Groups;
import se.kth.tablespoon.client.util.Sleep;


/**
 *
 * @author henke
 */
public class TopicStorageTest {
  
  /**
   * Test of add method, of class TopicStorage.
   */
  @Test
  public void testAdd() {
    System.out.println("add");
    TopicStorage storage = new TopicStorage(new Groups());
    Topic event = TopicFactory.create(storage, EventType.GROUP_AVERAGE, 2);
    storage.add(event);
    assertFalse(storage.isEmpty());
  }
  
  /**
   * Test of clean method, of class TopicStorage.
   */
  @Test
  public void testClean() {
    System.out.println("clean");
    Group group = new Group("1");
    Groups groups = new Groups();
    groups.add(group);
    group.getMachines().add("2");
    group.getMachines().add("3");
    TopicStorage storage = new TopicStorage(groups);
    Topic topic = TopicFactory.create(storage,EventType.REGULAR, 0);
    topic.addMachine("1");
    storage.add(topic);
    assertFalse(storage.isEmpty());
    
    storage.clean();
    assertTrue(storage.isEmpty());
    
    Topic topic2 = TopicFactory.create(storage,EventType.GROUP_AVERAGE, 2);
    topic2.addMachine("2");
    storage.add(topic2);
    storage.clean();
    assertFalse(storage.isEmpty());
    
    group.getMachines().remove("2");
    storage.clean();
    assertTrue(storage.isEmpty());
    
    Topic topic3 = TopicFactory.create(storage, EventType.REGULAR, 3);
    topic.addMachine("3");
    topic3.setDuration(1);
    storage.add(topic3);
    assertFalse(storage.isEmpty());
   
    int ms = 1100;
    System.out.println("Sleeping for " + ms + ".");
    Sleep.now(ms);
    storage.clean();
    assertTrue(storage.isEmpty());
  }
  
}
