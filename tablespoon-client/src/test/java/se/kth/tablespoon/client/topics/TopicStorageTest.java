/*
* To getAndChange this license header, choose License Headers in Project Properties.
* To getAndChange this template file, choose Tools | Templates
* and open the template in the topicitor.
*/
package se.kth.tablespoon.client.topics;

import org.junit.Test;
import se.kth.tablespoon.client.events.EventType;
import se.kth.tablespoon.client.events.Resource;
import static org.junit.Assert.*;
import se.kth.tablespoon.client.general.Group;
import se.kth.tablespoon.client.general.Groups;
import se.kth.tablespoon.client.util.Time;

/**
 *
 * @author henke
 */
public class TopicStorageTest {
  
  
  static int SLEEP_TIME = 1500;
  
  /**
   * Test of add method, of class TopicStorage.
   */
  @Test
  public void testAdd() {
    TopicStorage storage = new TopicStorage(new Groups());
    Resource resource = new Resource(2);
    Topic event = TopicFactory.create(storage,  resource, EventType.REGULAR, 1, null);
    storage.add(event);
    assertFalse(storage.isEmpty());
  }
  
  /**
   * Test of clean method, of class TopicStorage.
   */
  @Test
  public void testClean() {
    Group group = new Group("1");
    Groups groups = new Groups();
    groups.add(group);
    group.getMachines().add("2");
    group.getMachines().add("3");
    TopicStorage storage = new TopicStorage(groups);
    Resource resource = new Resource(0);
    Topic topic = TopicFactory.create(storage,  resource, EventType.REGULAR, 1, null);
    topic.addMachine("1");
    storage.add(topic);
    assertFalse(storage.isEmpty());
    
    storage.clean();
    assertTrue(storage.isEmpty());
    resource = new Resource(2);
    Topic topic2 = TopicFactory.create(storage,  resource, EventType.REGULAR, 1, null);
    topic2.addMachine("2");
    storage.add(topic2);
    storage.clean();
    assertFalse(storage.isEmpty());
    
    group.getMachines().remove("2");
    storage.clean();
    assertTrue(storage.isEmpty());
    resource = new Resource(3);
    Topic topic3 = TopicFactory.create(storage,  resource, EventType.REGULAR, 1, null);
    topic.addMachine("3");
    topic3.setDuration(1);
    storage.add(topic3);
    assertFalse(storage.isEmpty());
    
    Time.sleep(SLEEP_TIME);
    storage.clean();
    assertTrue(storage.isEmpty());
  }
  
}
