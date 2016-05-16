package se.kth.tablespoon.agent.listeners;


import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.kth.tablespoon.agent.events.Configuration;
import se.kth.tablespoon.agent.file.ConfigurationLoader;

import se.kth.tablespoon.agent.metrics.MetricLayout;
import se.kth.tablespoon.agent.metrics.MetricFormat;
import se.kth.tablespoon.agent.util.Sleep;

public class CollectlListenerTest {
  
  public CollectlListenerTest() {
  }
  
  @Test
  public void test() {
    ConfigurationLoader ch = new ConfigurationLoader();
    ch.readConfigFile();
    System.out.println("Configuration loaded.");
    Configuration config = ch.getConfig();
    System.out.println(config);
    CollectlListener cl = new CollectlListener(config);
    Thread t = new Thread(cl);
    t.start();
    assertTrue(t.isAlive());
    while(cl.queueIsEmpty()) {
      System.out.println("waiting for queue to fill...");
      Sleep.now(300);
    }
    assertEquals("User", cl.getEventLayouts()[0].getName());
    assertEquals(MetricFormat.PERSEC, cl.getEventLayouts()[9].getFormat());
    int i = 0;
    for (MetricLayout el : cl.getEventLayouts()) {
      assertNotNull("Iteration " + i + " failed.", el.getSource());
      i++;
    }
    cl.requestInterrupt();
    
    while(t.isAlive()) {
      System.out.println("waiting for thread to die...");
      Sleep.now(300);
    }
  }
  
  @Test
  public void restartTest() {
    ConfigurationLoader ch = new ConfigurationLoader();
    ch.readConfigFile();
    Configuration config = ch.getConfig();
    CollectlListener cl = new CollectlListener(config);
    Thread t = new Thread(cl);
    t.start();
    
    while(cl.queueIsEmpty()) {
      System.out.println("waiting for queue to fill...");
      Sleep.now(300);
    }
    
    cl.requestRestart();
    assertTrue(cl.isRestarting());
    
    while(cl.isRestarting()) {
      System.out.println("waiting for restart to finish...");
      Sleep.now(300);
    }
    
    cl.requestInterrupt();
    
    while(t.isAlive()) {
      System.out.println("waiting for thread to die...");
      Sleep.now(300);
    }
  }
  
}
