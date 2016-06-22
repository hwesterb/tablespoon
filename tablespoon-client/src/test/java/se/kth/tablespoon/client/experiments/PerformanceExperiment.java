package se.kth.tablespoon.client.experiments;

import io.riemann.riemann.client.RiemannClient;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.BeforeClass;
import org.junit.Test;
import se.kth.tablespoon.client.api.Subscriber;
import se.kth.tablespoon.client.api.TablespoonEvent;
import se.kth.tablespoon.client.broadcasting.RiemannEventFetcher;
import se.kth.tablespoon.client.events.EventType;
import se.kth.tablespoon.client.general.Group;
import se.kth.tablespoon.client.topics.GroupTopic;
import se.kth.tablespoon.client.topics.Topic;
import se.kth.tablespoon.client.util.Time;


public class PerformanceExperiment {
  
  static RiemannEventFetcher ref;
  static Group group;
  static RiemannClient rClient;
  static ExperimentSubscriber es;
  static ThreadPoolExecutor tpe;
  static final List<String> lines = Collections.synchronizedList(new ArrayList<String>());
  static final AtomicLong timeStamp = new AtomicLong(0);
  static final AtomicInteger counter = new AtomicInteger(0);
  static final String DIRECTORY = "";
  static final String RIEMANN_SERVER = "";
  static final String EXPERIMENT_NAME = "performance_experiment_aws";
  static final int EXPERIMENT_LIMIT = 60 * 10;
  static final int POOL_SIZE = 16;
  static final int NBR_OF_TOPICS= 10;
  static final int SEND_RATE = 600;
  static final int DELAY = 1000;
  
  
  @BeforeClass
  public static void setUpClass() throws IOException {
    tpe = new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE, 60, TimeUnit.SECONDS,
        new LinkedBlockingQueue<Runnable>());
    group = new Group("A");
    group.addMachine("1");
    es = new ExperimentSubscriber();
    rClient = RiemannClient.tcp(RIEMANN_SERVER, 5555);
    Time.sleep(1000);
    rClient.connect();
    Time.sleep(1000);
    while(rClient.isConnected() == false) {
      System.out.println("Waiting for connection to be accepted.");
      Time.sleep(100);
    }
    
  }
  
  @Test
  public void test() throws IOException {
    int nbrOfTopics = 69;
    ArrayList<Topic> topics = new ArrayList<>();
    for (int i = 0; i < nbrOfTopics; i++) {
      String topicId = "testTopic" + i;
      Topic topic = new GroupTopic(
          i,
          Time.now(),
          topicId,
          EventType.REGULAR,
          1,
          group
      );
      topic.setRetrievalDelay(DELAY);
      topic.createFetcher(es, rClient);
      topics.add(topic);
    }
    
    while(true) {
      for (Topic topic : topics) {
        topic.fetch(tpe);
      }
    }
  }
  
  
  private static class ExperimentSubscriber implements Subscriber {
    
    private final List<String> cpu = Collections.synchronizedList(new ArrayList<String>());
    private final List<String> mem = Collections.synchronizedList(new ArrayList<String>());
    private final List<String> net = Collections.synchronizedList(new ArrayList<String>());
    private final AtomicLong timeStamp = new AtomicLong(0);
    private final AtomicInteger counter = new AtomicInteger(0);
    
    @Override
    public void onEventArrival(TablespoonEvent event) {
      experiment(event);
      if (counter.get() <= NBR_OF_TOPICS)  {
        counter.incrementAndGet();
      }
    }
    
    private void experiment(TablespoonEvent event) {
      if (event == null) return;
      if (firstStep(event)) return;
      secondStep(event);
      
    }
    
    private boolean firstStep(TablespoonEvent event) {
      if (counter.get() == NBR_OF_TOPICS) {
        System.out.println("Initial counter fulfilled. Saving timeStamp: " + event.getTimeStamp());
        timeStamp.set(event.getTimeStamp());
      }
      else if (counter.get() < NBR_OF_TOPICS) {
        System.out.println("Preparing... this machine arrived: " + event.getMachineId());
        return true;
      }
      return false;
    }
    
    private void secondStep(TablespoonEvent event) {
      if (Time.now() - timeStamp.get() <= EXPERIMENT_LIMIT) {
        synchronized (this) {
          if (event.getCollectIndex() == 8) cpu.add(event.getTimeStamp() + " " + event.getValue());
          else if (event.getCollectIndex() == 22) mem.add(event.getTimeStamp() + " " + event.getValue());
          else if (event.getCollectIndex() == 53) net.add(event.getTimeStamp() + " " + event.getValue());
        }
      }
      else {
        writeStatistics();
        System.out.println("Experiment complete.");
        rClient.close();
        System.exit(0);
      }
    }
    
    synchronized void writeStatistics() {
      writeList("cpu", cpu);
      writeList("mem", mem);
      writeList("net", net);
    }
    
    
    private void writeList(String name, List<String> lines) {
      try {
        String fileName = DIRECTORY + EXPERIMENT_NAME + "_" +
            SEND_RATE + "_" +
            NBR_OF_TOPICS + "_" +
            POOL_SIZE + "_"  +
            DELAY + "_" +
            EXPERIMENT_LIMIT + "_" +
            name +
            ".csv";
        File file = new File(fileName);
        file.createNewFile();
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        for (String line : lines) {
          bw.write(line + "\n");
        }
        bw.close();
      } catch (IOException ex) {
        System.out.println("Could not write to file: " + ex.getMessage());
      }
    }
  }

  
  
}
