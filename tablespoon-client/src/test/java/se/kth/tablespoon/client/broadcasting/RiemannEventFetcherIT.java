package se.kth.tablespoon.client.broadcasting;

import io.riemann.riemann.client.RiemannClient;
import java.io.IOException;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import se.kth.tablespoon.client.api.SubscriberTester;
import se.kth.tablespoon.client.events.Comparator;
import se.kth.tablespoon.client.events.EventType;
import se.kth.tablespoon.client.events.Threshold;
import se.kth.tablespoon.client.general.Group;
import se.kth.tablespoon.client.topics.GroupTopic;
import se.kth.tablespoon.client.topics.Topic;
import se.kth.tablespoon.client.util.Time;


public class RiemannEventFetcherIT {
  
  static RiemannEventFetcher ref;
  static RiemannClient rClient;
  static SubscriberTester st;
  static double THRESHOLD = 10.3;
  
  @BeforeClass
  public static void setUpClass() throws IOException {
    String uniqueId = "a0986132-a503-4cee-ae85-83ac17ddcec1";
    Group group = new Group("A");
    group.addMachine("1");
    
    Topic topic = new GroupTopic(
        8,
        Time.now(),
        uniqueId,
        EventType.REGULAR,
        2,
        group
    );
    topic.setHigh(new Threshold(THRESHOLD, Comparator.LESS_THAN));
    System.out.println("Start this event on the agent:");
    topic.generateJson();
    System.out.println(topic.getJson());
    st = new SubscriberTester();
    ref = new RiemannEventFetcher(st, topic);
    rClient = RiemannClient.tcp("localhost", 5555);
    rClient.connect();
    while(rClient.isConnected() == false) {
      System.out.println("Waiting for connection to be accepted.");
      Time.sleep(100);
    }
  }
  
  
  @Test
  public void test() throws IOException {
    while(st.getCounter() < 2) {
      if (ref.shouldQuery()) ref.queryRiemannAndSend(rClient);
      if (st.getEvent() != null) assertTrue(st.getEvent().getValue() < THRESHOLD);
      Time.sleep(400);
    }
  }
  
}
