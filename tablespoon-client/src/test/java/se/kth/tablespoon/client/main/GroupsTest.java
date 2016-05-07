/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.client.main;

import java.util.ArrayList;
import java.util.Iterator;
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
public class GroupsTest {

  /**
   * Test of add method, of class Groups.
   */
  @Test
  public void testAdd() {
    System.out.println("add");
    Group group = null;
    Groups instance = new Groups();
    instance.add(group);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of get method, of class Groups.
   */
  @Test
  public void testGet() {
    System.out.println("get");
    String groupId = "";
    Groups instance = new Groups();
    Group expResult = null;
    Group result = instance.get(groupId);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of remove method, of class Groups.
   */
  @Test
  public void testRemove() {
    System.out.println("remove");
    String groupId = "";
    Groups instance = new Groups();
    instance.remove(groupId);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of takeSnapshop method, of class Groups.
   */
  @Test
  public void testTakeSnapshop() {
    System.out.println("takeSnapshop");
    Groups instance = new Groups();
    instance.takeSnapshop();
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of retainWithSnapshot method, of class Groups.
   */
  @Test
  public void testRetainWithSnapshot() {
    System.out.println("retainWithSnapshot");
    ArrayList<String> machines = null;
    Groups instance = new Groups();
    instance.retainWithSnapshot(machines);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of iterator method, of class Groups.
   */
  @Test
  public void testIterator() {
    System.out.println("iterator");
    Groups instance = new Groups();
    Iterator<String> expResult = null;
    Iterator<String> result = instance.iterator();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
