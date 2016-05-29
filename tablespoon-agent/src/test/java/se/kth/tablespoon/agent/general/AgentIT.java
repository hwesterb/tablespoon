/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.agent.general;

import se.kth.tablespoon.agent.events.Configuration;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import se.kth.tablespoon.agent.events.Topics;
import se.kth.tablespoon.agent.listeners.MetricListener;
import se.kth.tablespoon.agent.metrics.Metric;
import se.kth.tablespoon.agent.metrics.MetricFormat;
import se.kth.tablespoon.agent.events.ResourceType;
import se.kth.tablespoon.agent.util.Time;

/**
 *
 * @author te27
 */
public class AgentIT {

  static Configuration config = Configuration.getInstance();
  static MetricListener ml =  new MetricListenerTester();
  static Agent agent;

  public AgentIT() {
  }

  @BeforeClass
  public static void setUpClass() {
    ListeningServer ls = new ListeningServer();
    ls.run();
    agent = new Agent(ml, new Topics());

    Thread mlThread = new Thread(ml);
    mlThread.start();
    while (ml.globalIsEmpty()) {
      System.out.println("waiting...");
      Time.sleep(500);
    }
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  private static class ListeningServer implements Runnable {

    ServerSocket srvSocket;

    @Override
    public void run() {
      try {
        srvSocket = new ServerSocket(2727);
        Socket accept = srvSocket.accept();
        while (true) {
          System.out.println(accept.getInputStream().available());
        }
      } catch (IOException ex) {
        Logger.getLogger(AgentIT.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  private static class MetricListenerTester extends MetricListener {

    public MetricListenerTester() {
      super();
    }

    @Override
    public void collectCycle() {
      while (true) {
        globalQueue.add(new Metric(1, ResourceType.DSK, MetricFormat.TOTAL, Time.now(), "agenttester", 27));
        Time.sleep(100);
      }
    }

    @Override
    protected void createCustomMetrics(ArrayList<Metric> metrics) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

  }

}
