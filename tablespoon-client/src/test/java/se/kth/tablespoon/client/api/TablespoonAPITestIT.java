package se.kth.tablespoon.client.api;


import org.junit.BeforeClass;
import org.junit.Test;
import se.kth.tablespoon.client.broadcasting.AgentBroadcasterAssistant;
import se.kth.tablespoon.client.events.EventType;
import se.kth.tablespoon.client.events.Resource;
import se.kth.tablespoon.client.events.ResourceType;
import se.kth.tablespoon.client.general.Group;
import se.kth.tablespoon.client.general.Groups;
import se.kth.tablespoon.client.topics.MissingTopicException;
import se.kth.tablespoon.client.topics.ThresholdException;
import se.kth.tablespoon.client.topics.TopicStorage;
import se.kth.tablespoon.client.util.Time;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TablespoonAPITestIT {

    static final int SLEEP_TIME = 3000;
    static Groups groups;
    static Group groupA;
    static Group groupB;
    static TopicStorage storage;
    static AgentBroadcasterAssistant aba;
    static Thread abaThread;
    static AgentBroadcasterTester abt;
    static TestSubscriber subscriberB;
    static TestSubscriber subscriberA;
    static SubscriberBroadcasterTester sbt;
    static TablespoonApi api;

    @BeforeClass
    public static void setUp() {
        System.out.println("This test involves waiting for threads...");
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
        subscriberB = new TestSubscriber();
        subscriberA = new TestSubscriber();
        Time.sleep(SLEEP_TIME*2);
        sbt = new SubscriberBroadcasterTester();
        api = new TablespoonApi(storage, groups, sbt);
        aba.registerBroadcaster(abt);
        Time.sleep(SLEEP_TIME);
    }

    @Test
    public void testCreateTopic() throws ThresholdException, MissingTopicException, MissingParameterException, IOException {
        String groupId = "B";
        EventType eventType = EventType.REGULAR;
        Resource resource = new Resource(ResourceType.CPU);
        int duration = 10;
        int sendRate = 2;
        String uniqueId = api.submitter().
                subscriber(subscriberB).
                groupId(groupId).
                eventType(eventType).
                resource(resource).
                duration(duration).
                sendRate(sendRate).
                submit();
        subscriberB.uniqueId = uniqueId;

        uniqueId = api.submitter().
                subscriber(subscriberB).
                groupId(groupId).
                eventType(eventType).
                resource(resource).
                duration(duration).
                sendRate(6).
                submit();
        Time.sleep(10000);
    }

}
