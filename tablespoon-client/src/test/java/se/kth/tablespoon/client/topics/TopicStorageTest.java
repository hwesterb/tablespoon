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
  
<<<<<<< Updated upstream
  /**
   * Test of add method, of class TopicStorage.
   */
||||||| merged common ancestors
  
  static int SLEEP_TIME = 1500;
  
  /**
   * Test of add method, of class TopicStorage.
   */
=======
  
  static int SLEEP_TIME = 2000;
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
  
>>>>>>> Stashed changes
  @Test
  public void testAdd() {
<<<<<<< Updated upstream
    System.out.println("add");
    TopicStorage storage = new TopicStorage(new Groups());
    Resource resource = new Resource(2);
    Topic event = TopicFactory.create(storage,  resource, EventType.REGULAR, 1, null);
    storage.add(event);
||||||| merged common ancestors
    TopicStorage storage = new TopicStorage(new Groups());
    Resource resource = new Resource(2);
    Topic event = TopicFactory.create(storage,  resource, EventType.REGULAR, 1, null);
    storage.add(event);
=======
    HashSet<String> machines = new HashSet<>();
    machines.add("1");
    Topic topic = TopicFactory.createMachineTopic(storage.generateUniqueId(),
        resource, EventType.REGULAR, 1, machines);
    storage.add(topic);
>>>>>>> Stashed changes
    assertFalse(storage.isEmpty());
  }
  
  @Test
<<<<<<< Updated upstream
  public void testClean() {
    System.out.println("clean");
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
    
||||||| merged common ancestors
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
    
=======
  public void testCleanNotPresent() {
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
    resource = new Resource(3);
    Topic topic3 = TopicFactory.create(storage,  resource, EventType.REGULAR, 1, null);
    topic.addMachine("3");
    topic3.setDuration(1);
    storage.add(topic3);
    assertFalse(storage.isEmpty());
   
    int ms = 1100;
    System.out.println("Sleeping for " + ms + ".");
    Time.sleep(ms);
||||||| merged common ancestors
    resource = new Resource(3);
    Topic topic3 = TopicFactory.create(storage,  resource, EventType.REGULAR, 1, null);
    topic.addMachine("3");
    topic3.setDuration(1);
    storage.add(topic3);
    assertFalse(storage.isEmpty());
    
    Time.sleep(SLEEP_TIME);
=======
    
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
>>>>>>> Stashed changes
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
