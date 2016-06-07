package se.kth.tablespoon.client.general;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import org.junit.BeforeClass;
import org.junit.Test;
import se.kth.tablespoon.client.api.MissingParameterException;
import se.kth.tablespoon.client.api.Subscriber;
import se.kth.tablespoon.client.api.TablespoonAPI;
import se.kth.tablespoon.client.api.TablespoonEvent;
import se.kth.tablespoon.client.broadcasting.AgentBroadcaster;
import se.kth.tablespoon.client.broadcasting.BroadcastException;
import se.kth.tablespoon.client.events.EventType;
import se.kth.tablespoon.client.events.Resource;
import se.kth.tablespoon.client.events.ResourceType;
import se.kth.tablespoon.client.topics.MissingTopicException;
import se.kth.tablespoon.client.topics.ThresholdException;
import se.kth.tablespoon.client.util.Time;


public class StartIT {
  
  private static Groups groups;
  private static SubscriberForIntegerationTest subscriber;
  private static String groupId;
  private static String machineId;
  
  @BeforeClass
  public static void setUpClass() {
    groups = new Groups();
    AgentBroadcasterForIntegerationTest ab = new AgentBroadcasterForIntegerationTest();
    Start.setUp(groups, ab, "localhost", 5555);
    groupId = "A";
    machineId = "1";
    Group group = new Group(groupId);
    group.addMachine(machineId);
    groups.add(group);
  }
  
  @Test
  public void test() throws ThresholdException, MissingTopicException, MissingParameterException, IOException {
    subscriber = new SubscriberForIntegerationTest();
    EventType eventType = EventType.GROUP_AVERAGE;
    Resource resource = new Resource(ResourceType.CPU);
    int sendRate = 3;
    String uniqueId = TablespoonAPI.
        getInstance().
        submitter().
        subscriber(subscriber).
        groupId(groupId).
        eventType(eventType).
        resource(resource).
        sendRate(sendRate).
        duration(20).
        submit();
    while(subscriber.getRecievedRequests() == 0) {
      Time.sleep(1000);
      System.out.println("Waiting for requests");
    }
    System.out.println(subscriber.recentEvent);
  }
  
  
  public static class SubscriberForIntegerationTest implements Subscriber {
    
    private int recievedRequests;
    private TablespoonEvent recentEvent;
    
    public int getRecievedRequests() {
      return recievedRequests;
    }

    public TablespoonEvent getRecentEvent() {
      return recentEvent;
    }
    
    @Override
    public void onEventArrival(TablespoonEvent event) {
      recievedRequests++;
      recentEvent = event;
    }
    
  }
  
  private static class AgentBroadcasterForIntegerationTest implements AgentBroadcaster {
    @Override
    public void sendToMachines(Set<String> machines, String json, String topicId) throws BroadcastException {
      System.out.println("Sending to machines " + machines);
      System.out.println("This is the json\n " +  json);
      try {
        String fileName = "/usr/local/tablespoon-agent/topics/"
            + topicId + ".json";
        System.out.println("Attempting to write " + fileName);
        File file = new File(fileName);
        // if file doesnt exists, then create it
        if (!file.exists()) {
          file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(json);
        bw.close();
        
        System.out.println("Wrote " + fileName);
        
      } catch (IOException e) {
        e.printStackTrace();
        System.out.println("Couldn't write file. Aborting test");
        System.exit(0);
      }
      
    }
    
  }
  
  
}
