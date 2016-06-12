package se.kth.tablespoon.client.broadcasting;

import io.riemann.riemann.client.RiemannClient;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.junit.BeforeClass;
import org.junit.Test;
import se.kth.tablespoon.client.api.SubscriberTester;
import se.kth.tablespoon.client.events.EventType;
import se.kth.tablespoon.client.general.Group;
import se.kth.tablespoon.client.topics.GroupTopic;
import se.kth.tablespoon.client.topics.Topic;
import se.kth.tablespoon.client.util.Time;


public class RiemannEventFetcherIT {
  
  static RiemannEventFetcher ref;
  static Topic topic;
  static RiemannClient rClient;
  static SubscriberTester st;
  static final ThreadPoolExecutor tpe = new ThreadPoolExecutor(100, 100, 60, TimeUnit.SECONDS,
      new LinkedBlockingQueue<Runnable>());
  
  @BeforeClass
  public static void setUpClass() throws IOException {
    String uniqueId = "a0986132-a503-4cee-ae85-83ac17ddcec";
    Group group = new Group("A");
    group.addMachine("1");
    topic = new GroupTopic(
        0,
        Time.now(),
        uniqueId,
        EventType.REGULAR,
        1,
        group
    );
    System.out.println("Start this event on the agent:");
    topic.generateJson();
    System.out.println(topic.getJson());
    st = new SubscriberTester();
    rClient = RiemannClient.tcp("localhost", 5555);
    rClient.connect();
    while(rClient.isConnected() == false) {
      System.out.println("Waiting for connection to be accepted.");
      Time.sleep(100);
    }
    topic.createFetcher(st, rClient);
  }
  
  @Test
  public void testLatencyExperiment() throws IOException {
    while(true) {
      topic.fetch(tpe);
      Time.sleep(1);
    }
  }
  
}
