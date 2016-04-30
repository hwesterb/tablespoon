/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.event.processing.communication;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author henke
 */
public class RiemannListenerTest {
  
  public RiemannListenerTest() {
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
   * Test of listen method, of class RiemannListener.
   */
  @org.junit.Test
  public void testListen() throws Exception {
    System.out.println("listen");
    int portNumber = 0;
    RiemannListener instance = null;
    instance.listen(portNumber);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of run method, of class RiemannListener.
   */
  @org.junit.Test
  public void testRun() {
    System.out.println("run");
    RiemannListener instance = null;
    instance.run();
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of requestInterrupt method, of class RiemannListener.
   */
  @org.junit.Test
  public void testRequestInterrupt() {
    System.out.println("requestInterrupt");
    RiemannListener instance = null;
    instance.requestInterrupt();
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
