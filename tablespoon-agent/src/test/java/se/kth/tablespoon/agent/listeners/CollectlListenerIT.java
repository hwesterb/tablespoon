package se.kth.tablespoon.agent.listeners;

import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import se.kth.tablespoon.agent.events.Configuration;
import se.kth.tablespoon.agent.file.ConfigurationLoader;
import se.kth.tablespoon.agent.file.JsonException;

import se.kth.tablespoon.agent.metrics.MetricLayout;
import se.kth.tablespoon.agent.metrics.MetricFormat;
import se.kth.tablespoon.agent.util.Time;

public class CollectlListenerIT {
  
  public CollectlListenerIT() {
  }
  
  static Configuration config;
  
  @BeforeClass
  public static void setUp() throws JsonException, IOException {
    (new ConfigurationLoader()).readConfigFile();
    config = Configuration.getInstance();
    System.out.println(config);
  }
  
  @Test
  public void test() {
    CollectlListener cl = new CollectlListener();
    Thread t = new Thread(cl);
    t.start();
    assertTrue(t.isAlive());
    while(cl.metricQueueIsEmpty()) {
      System.out.println("waiting for queue to fill...");
      Time.sleep(300);
    }
    assertEquals("User", cl.getEventLayouts()[0].getName());
    assertEquals(MetricFormat.PERSEC, cl.getEventLayouts()[9].getFormat());
    assertEquals("Tot", cl.getEventLayouts()[19].getName());
    assertEquals("Used", cl.getEventLayouts()[20].getName());
    assertEquals(65, cl.getEventLayouts().length);
    // 65 in every collection + 1 custom = 66
    assertEquals(0, cl.getMetricQueue().size() % 66);
    int i = 0;
    for (MetricLayout el : cl.getEventLayouts()) {
      assertNotNull("Iteration " + i + " failed.", el.getSource());
      i++;
    }
    
    cl.requestInterrupt();
    
    while(t.isAlive()) {
      System.out.println("waiting for thread to die...");
      Time.sleep(300);
    }
  }
  
  @Test
  public void restartTest() {
    CollectlListener cl = new CollectlListener();
    Thread t = new Thread(cl);
    t.start();
    
    while(cl.metricQueueIsEmpty()) {
      System.out.println("waiting for queue to fill...");
      Time.sleep(300);
    }
    
    cl.requestRestart();
    assertTrue(cl.isRestarting());
    
    while(cl.isRestarting()) {
      System.out.println("waiting for restart to finish...");
      Time.sleep(300);
    }
    
    cl.requestInterrupt();
    
    while(t.isAlive()) {
      System.out.println("waiting for thread to die...");
      Time.sleep(300);
    }
  }
  
}
