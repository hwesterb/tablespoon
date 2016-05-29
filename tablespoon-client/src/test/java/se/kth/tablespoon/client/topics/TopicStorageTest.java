/*
* To getAndChange this license header, choose License Headers in Project Properties.
* To getAndChange this template file, choose Tools | Templates
* and open the template in the topicitor.
*/
package se.kth.tablespoon.client.topics;

import java.util.HashSet;
import org.junit.Test;
import se.kth.tablespoon.client.events.EventType;
import se.kth.tablespoon.client.events.Resource;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import se.kth.tablespoon.client.general.Group;
import se.kth.tablespoon.client.general.Groups;
import se.kth.tablespoon.client.util.Time;

/**
 *
 * @author henke
 */
public class TopicStorageTest {
  
  static int SLEEP_TIME = 3000;
  static TopicStorage storage;
  static Resource resource;
  static Groups groups;
  static Group group;
  
  @BeforeClass
  public static void beforeClass() {
    resource = new Resource(0);
    groups = new Groups();
    group = new Group("A");
    group.addMachine("2");
    group.addMachine("3");
    group.addMachine("4");
    group.addMachine("5");
    group.addMachine("6");
    groups.add(group);
    storage = new TopicStorage(groups);
  }
  

  @Test
  public void testAdd() {
    HashSet<String> machines = new HashSet<>();
    machines.add("1");
    Topic topic = TopicFactory.createMachineTopic(storage.generateUniqueId(),
        resource, EventType.REGULAR, 1, machines);
    storage.add(topic);
    assertFalse(storage.isEmpty());
  }
  

  public void testCleanNotPresent() {
    storage.clean();
    assertTrue(storage.isEmpty());
  }
  
  @Test
  public void testCleanPresent() {
    HashSet<String> machines = new HashSet<>();
    machines.add("2");
    Topic topic = TopicFactory.createMachineTopic(storage.generateUniqueId(),
        resource, EventType.REGULAR, 1, machines);
    storage.add(topic);
    storage.clean();
    assertFalse(storage.isEmpty());
  }
  
  
  @Test
  public void testCleanNoLongerPresent() {
    group.removeMachine("2");
    storage.clean();
    assertTrue(storage.isEmpty());    
  }
  
  @Test
  public void testCleanWithDuration() {
    HashSet<String> machines = new HashSet<>();
    machines.add("3");
    Topic topic = TopicFactory.createMachineTopic(storage.generateUniqueId(),
        resource, EventType.REGULAR, 1, machines);
    topic.setDuration(1);
    storage.add(topic);
    assertFalse(storage.isEmpty());
    Time.sleep(SLEEP_TIME);
    storage.clean();
    assertTrue(storage.isEmpty());
  }
  @Test
  public void testCleanGroupClear() {
    Topic topic = TopicFactory.createGroupTopic(storage.generateUniqueId(),
        resource, EventType.REGULAR, 1, group);
    storage.add(topic);
    assertFalse(storage.isEmpty());
    group.clear();
    storage.clean();
    assertTrue(storage.isEmpty());
  }
  
  
}
