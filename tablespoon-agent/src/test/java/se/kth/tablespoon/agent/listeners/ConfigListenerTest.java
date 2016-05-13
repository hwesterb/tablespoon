/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.agent.listeners;

import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import se.kth.tablespoon.agent.file.ConfigurationLoader;
import se.kth.tablespoon.agent.util.Sleep;

/**
 *
 * @author te27
 */
public class ConfigListenerTest {

  public ConfigListenerTest() {
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
   * Test of look method, of class ConfigListener.
   */
  @Test
  public void testLook() throws Exception {
    System.out.println("look");
    ConfigurationLoader ch = new ConfigurationLoader();
    ConfigListener cl = new ConfigListener(ch);

    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    int numFiles = (new File(classLoader.getResource("configuration").getFile()).listFiles().length);
    assertEquals(numFiles, 1);  // tests if files in directory is more than one
  }

  @Test
  public void testLookMethod() {
    ConfigurationLoader ch = new ConfigurationLoader();
    ConfigListener cl = new ConfigListener(ch);
    Thread clt = new Thread(cl);
    clt.start();
    Sleep.now(5000);
    cl.requestInterrupt();
    Sleep.now(1000);
    assertFalse(clt.isAlive());
  }

  /**
   * Test of requestInterrupt method, of class ConfigListener.
   */
  @Test
  public void testRequestInterrupt() throws IOException {
    System.out.println("requestInterrupt");
    ConfigurationLoader ch = new ConfigurationLoader();
    ConfigListener cl = new ConfigListener(ch);
    Thread clt = new Thread(cl);
    clt.start();

    cl.requestInterrupt();
    Sleep.now(1000);
    assertFalse(clt.isAlive());
  }

  @Test
  public void testConfigFile() throws IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    File file = new File(classLoader.getResource("configuration/config.json").getFile());
    assertTrue(file.exists());
  }

}
