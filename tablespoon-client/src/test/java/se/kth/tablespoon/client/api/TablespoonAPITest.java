
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.api;

import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import se.kth.tablespoon.client.broadcasting.AgentBroadcasterAssistant;
import se.kth.tablespoon.client.general.Group;
import se.kth.tablespoon.client.general.Groups;
import se.kth.tablespoon.client.events.Comparator;
import se.kth.tablespoon.client.events.EventType;
import se.kth.tablespoon.client.events.Resource;
import se.kth.tablespoon.client.events.ResourceType;
import se.kth.tablespoon.client.events.Threshold;
import se.kth.tablespoon.client.topics.MissingTopicException;
import se.kth.tablespoon.client.topics.ThresholdException;
import se.kth.tablespoon.client.topics.TopicStorage;
import se.kth.tablespoon.client.util.Time;

/**
 *
 * @author henke
 */
public class TablespoonAPITest {
  
  static final int SLEEP_TIME = 500;
  static Groups groups;
  static Group groupA;
  static Group groupB;
  static TopicStorage storage;
  static AgentBroadcasterAssistant aba;
  static Thread abaThread;
  static AgentBroadcasterTester abt;
  static SubscriberTester subscriberB;
  static SubscriberTester subscriberA;
  static SubscriberBroadcasterTester sbt;
  static TablespoonAPI api;
  
  @BeforeClass
  public static void setUp() {
    groups = new Groups();
    storage = new TopicStorage(groups);
    aba = new AgentBroadcasterAssistant(storage);
    abaThread = new Thread(aba);
    abaThread.start();
    groupA = new Group("A");
    groupA.addMachine("0");
    groupA.addMachine("1");
    groupA.addMachine("2");
    groupA.addMachine("3");
    groups.add(groupA);
    groupB = new Group("B");
    groupB.addMachine("4");
    groupB.addMachine("5");
    groups.add(groupB);
    abt = new AgentBroadcasterTester();
    subscriberB = new SubscriberTester();
    subscriberA = new SubscriberTester();
<<<<<<< Updated upstream
    Time.sleep(1000); //wait for threads to start
||||||| merged common ancestors
    Time.sleep(SLEEP_TIME*2); 
=======
    Time.sleep(SLEEP_TIME*2);
>>>>>>> Stashed changes
    sbt = new SubscriberBroadcasterTester();
    api = TablespoonAPI.getInstance();
    api.prepareAPI(storage, groups, sbt);
  }
  
  
  @AfterClass
  public static void tearDown() {
    Time.sleep(100); //get thread into wait position
  }
  
    /**
   * Test of createTopic method, of class TablespoonAPI.
   */
  @Test
<<<<<<< Updated upstream
  public void testCreateTopic() {
    System.out.println("\n*** createTopic ***\n");
||||||| merged common ancestors
  public void testCreateTopic() {
=======
  public void testCreateTopic() throws ThresholdException, MissingTopicException, MissingParameterException {
>>>>>>> Stashed changes
    String groupId = "B";
    EventType eventType = EventType.REGULAR;
    Resource resource = new Resource(ResourceType.CPU);
    int duration = 10;
    int sendRate = 2;
<<<<<<< Updated upstream
    subscriberB.setUniqueId(api.createTopic(subscriberB, groupId, eventType, resource, duration, sendRate));
    aba.registerBroadcaster(abt);
||||||| merged common ancestors
    subscriberB.setUniqueId(api.createTopic(subscriberB, groupId, eventType, resource, duration, sendRate));
=======
    String uniqueId = api.submitter().
        subscriber(subscriberB).
        groupId(groupId).
        eventType(eventType).
        resource(resource).
        duration(duration).
        sendRate(sendRate).
        submit();
    subscriberB.setUniqueId(uniqueId);
>>>>>>> Stashed changes
    Time.sleep(SLEEP_TIME);
    assertEquals(2, abt.getRecievedRequests());
  }
  
  /**
   * Test of changeTopic method, of class TablespoonAPI.
   */
  @Test
<<<<<<< Updated upstream
  public void testChangeTopic() throws Exception {
    System.out.println("\n*** changeTopic ***\n");
    api.changeTopic(subscriberB.getUniqueId(), new Threshold(80.0, Comparator.LESS_THAN));
||||||| merged common ancestors
  public void testChangeTopic() throws Exception {
    api.changeTopic(subscriberB.getUniqueId(), new Threshold(80.0, Comparator.LESS_THAN));
=======
  public void replicateTopic() throws Exception {
    api.submitter().
        subscriber(subscriberB).
        replaces(subscriberB.getUniqueId(), true).
        high(new Threshold(80.0, Comparator.LESS_THAN)).
        submit();
>>>>>>> Stashed changes
    Time.sleep(SLEEP_TIME);
    assertEquals(4, abt.getRecievedRequests());
  }
//
//   @Test
//  public void createTopic() throws Exception {
//    api.submitter().
//        subscriber(subscriberB).
//        replaces(subscriberB.getUniqueId(), true).
//        high(new Threshold(80.0, Comparator.LESS_THAN)).
//        submit();
//    Time.sleep(SLEEP_TIME);
//    assertEquals(4, abt.getRecievedRequests());
//  }
  
  /**
   * Test of createTopic method, of class TablespoonAPI.
   */
  @Test
  public void testCreateSecondTopic() throws Exception {
    System.out.println("\n*** secondTopic ***\n");
    Threshold high = new Threshold(60.0, Comparator.GREATER_THAN);
    Threshold low = new Threshold(30.0, Comparator.LESS_THAN);
    Resource resource = new Resource(ResourceType.CPU);
    String uniqueId = api.submitter().
        subscriber(subscriberA).
        groupId("A").
        eventType(EventType.GROUP_AVERAGE).
        resource(resource).
        sendRate(2).
        low(low).
        high(high).
        submit();
    subscriberA.setUniqueId(uniqueId);
    Time.sleep(SLEEP_TIME);
    assertEquals(8, abt.getRecievedRequests());
  }
  
  /**
   * Test of removeTopic method, of class TablespoonAPI.
   */
  @Test
  public void testRemoveTopic() throws Exception {
    System.out.println("\n*** removeTopic ***\n");
    api.removeTopic(subscriberA.getUniqueId());
    Time.sleep(SLEEP_TIME);
    assertEquals(12, abt.getRecievedRequests());
  }  
  
}
