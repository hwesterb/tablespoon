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
   * Test of loadNewConfiguration method, of class ConfigurationLoader.
   */
  @Test
  public void testLoadNewConfiguration() {
    System.out.println("loadNewConfiguration");
    ConfigurationLoader instance = new ConfigurationLoader();
    instance.loadNewConfiguration();
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setMetricListener method, of class ConfigurationLoader.
   */
  @Test
  public void testSetMetricListener() {
    System.out.println("setMetricListener");
    MetricListener metricListener = null;
    ConfigurationLoader instance = new ConfigurationLoader();
    instance.setMetricListener(metricListener);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getConfig method, of class ConfigurationLoader.
   */
  @Test
  public void testGetConfig() {
    System.out.println("getConfig");
    ConfigurationLoader instance = new ConfigurationLoader();
    Configuration expResult = null;
    Configuration result = instance.getConfig();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
