package se.kth.tablespoon.client.broadcasting;

import io.riemann.riemann.client.RiemannClient;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.junit.BeforeClass;
import org.junit.Test;
import se.kth.tablespoon.client.api.TestSubscriber;
import se.kth.tablespoon.client.events.EventType;
import se.kth.tablespoon.client.general.Group;
import se.kth.tablespoon.client.topics.GroupTopic;
import se.kth.tablespoon.client.topics.Topic;
import se.kth.tablespoon.client.util.Time;


public class RiemannEventFetcherIT {
  static RiemannEventFetcher ref;
  static Group group;
  static RiemannClient rClient;
  static TestSubscriber st;
  static final ThreadPoolExecutor tpe = new ThreadPoolExecutor(100, 100, 60, TimeUnit.SECONDS,
      new LinkedBlockingQueue<Runnable>());
  
  @BeforeClass
  public static void setUpClass() throws IOException {
    
    group = new Group("A");
    group.addMachine("1");
    
    st = new TestSubscriber();
    rClient = RiemannClient.tcp("localhost", 5555);
    rClient.connect();
    while(rClient.isConnected() == false) {
      System.out.println("Waiting for connection to be accepted.");
      Time.sleep(100);
    }
    
  }
  
  @Test
  public void test() throws IOException {
    String uniqueId = "a0986132-a503-4cee-ae85-83ac17ddcec";
    Topic topic = new GroupTopic(
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
    topic.createFetcher(st, rClient);
    while(true) {
      topic.fetch(tpe);
      Time.sleep(1);
    }
    
  }
  
 
}
