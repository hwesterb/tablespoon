/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.client.api;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.kth.tablespoon.client.broadcasting.AgentBroadcasterAssistant;
import se.kth.tablespoon.client.general.Group;
import se.kth.tablespoon.client.general.Groups;
import se.kth.tablespoon.client.events.Comparator;
import se.kth.tablespoon.client.events.EventType;
import se.kth.tablespoon.client.events.ResourceType;
import se.kth.tablespoon.client.events.Threshold;
import se.kth.tablespoon.client.topics.TopicStorage;
import se.kth.tablespoon.client.util.Time;

/**
 *
 * @author te27
 */
public class DummieEventGeneratorTest {

  static Groups groups;
  static Group groupA;
  static Group groupB;
  static TopicStorage storage;
  static AgentBroadcasterAssistant aba;
  static Thread abaThread;
  static AgentBroadcasterTester abt;
  static SubscriberTester subscriberB;
  static SubscriberTester subscriberA;
  static TablespoonAPI api;
  static int SLEEP_TIME = 300;

  public DummieEventGeneratorTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
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
    Time.sleep(SLEEP_TIME); //wait for threads to start
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testWithNoThresholds() {
    System.out.println("\n*** NoThreshold ***\n");
    String groupId = "B";
    EventType eventType = EventType.REGULAR;
    ResourceType resourceType = ResourceType.CPU;
    int duration = 100;
    DummieEventGenerator deg = new DummieEventGenerator(subscriberB, groupId,
        eventType, resourceType, duration, null, null);
    Thread degThread = new Thread(deg);
    degThread.start();
    Time.sleep(SLEEP_TIME);
  }

  @Test
  public void testWithOneThresholds() {
    System.out.println("\n*** OneThreshold ***\n");
    String groupId = "B";
    EventType eventType = EventType.REGULAR;
    ResourceType resourceType = ResourceType.CPU;
    Threshold th = new Threshold(30, Comparator.GREATER_THAN_OR_EQUAL);
    int duration = 1000;
    DummieEventGenerator deg = new DummieEventGenerator(subscriberB, groupId,
        eventType, resourceType, duration, th, null);
    Thread degThread = new Thread(deg);
    degThread.start();
    Time.sleep(SLEEP_TIME);
  }

  @Test
  public void testWithTwoThresholds() {
    System.out.println("\n*** TwoThresholds ***\n");
    String groupId = "B";
    EventType eventType = EventType.REGULAR;
    ResourceType resourceType = ResourceType.CPU;
    Threshold th = new Threshold(90, Comparator.GREATER_THAN_OR_EQUAL);
    Threshold tl = new Threshold(10, Comparator.LESS_THAN_OR_EQUAL);
    int duration = 1000;
    DummieEventGenerator deg = new DummieEventGenerator(subscriberB, groupId,
        eventType, resourceType, duration, th, tl);
    Thread degThread = new Thread(deg);
    degThread.start();
    Time.sleep(SLEEP_TIME);
  }
  
}
