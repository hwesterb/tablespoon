/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.agent.general;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import se.kth.tablespoon.agent.listeners.MetricListener;

/**
 *
 * @author te27
 */
public class ConfigurationHandlerTest {
  
  public ConfigurationHandlerTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
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

  /**
   * Test of loadNewConfiguration method, of class ConfigurationHandler.
   */
  @Test
  public void testLoadNewConfiguration() {
    System.out.println("loadNewConfiguration");
    ConfigurationHandler instance = new ConfigurationHandler();
    instance.loadNewConfiguration();
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setMetricListener method, of class ConfigurationHandler.
   */
  @Test
  public void testSetMetricListener() {
    System.out.println("setMetricListener");
    MetricListener metricListener = null;
    ConfigurationHandler instance = new ConfigurationHandler();
    instance.setMetricListener(metricListener);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getConfig method, of class ConfigurationHandler.
   */
  @Test
  public void testGetConfig() {
    System.out.println("getConfig");
    ConfigurationHandler instance = new ConfigurationHandler();
    Configuration expResult = null;
    Configuration result = instance.getConfig();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
