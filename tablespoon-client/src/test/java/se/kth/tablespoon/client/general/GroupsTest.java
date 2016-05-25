/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.general;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class GroupsTest {
  
  static Groups groups;
  static Group group;
  static Group group2;
  
  @BeforeClass
  public static void setUp() {
    groups = new Groups();
    group = new Group("B");
    group2 = new Group("A");
    group.addMachine("0");
    group.addMachine("1");
    group.addMachine("2");
    group.addMachine("3");
    groups.add(group);
    group2.addMachine("4");
    group2.addMachine("5");
    groups.add(group2);
  }
  
  /**
   * Test of retainWithSnapshot method, of class Groups.
   */
  @Test
  public void testRetainWithSnapshot() {
    groups.takeSnapshop();
    ArrayList<String> machines = new ArrayList<>();
    machines.add("1");
    machines.add("4");
    machines.add("19");
    groups.retainWithSnapshot(machines);
    assertEquals(2, machines.size());
  }
  
}
