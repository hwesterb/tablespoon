/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.general;

<<<<<<< Updated upstream
import se.kth.tablespoon.client.general.Group;
import se.kth.tablespoon.client.general.Groups;
import java.util.ArrayList;
||||||| merged common ancestors
import java.util.ArrayList;
=======
import java.util.HashSet;
>>>>>>> Stashed changes
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author henke
 */
public class GroupsTest {
  
  static Groups groups;
  static Group group;
  static Group group2;
  
  @BeforeClass
  public static void setUp() {
    groups = new Groups();
    group = new Group("B");
    group2 = new Group("A");
  }
  
  /**
   * Test of add method, of class Groups.
   */
  @Test
  public void testAdd() {
    System.out.println("add");
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
   * Test of iterator method, of class Groups.
   */
  @Test
  public void testIterator() {
    System.out.println("iterator");
    int i = 0;
    for (String g : groups) {
      i++;
      System.out.println(g);
    }
    assertEquals(6, i);
    
  }
  
  /**
   * Test of retainWithSnapshot method, of class Groups.
   */
  @Test
  public void testRetainWithSnapshot() {
    System.out.println("retainWithSnapshot");
    groups.takeSnapshop();
    HashSet<String> machines = new HashSet<>();
    machines.add("1");
    machines.add("4");
    machines.add("19");
    groups.retainWithSnapshot(machines);
    assertEquals(2, machines.size());
  }
  
  
  
}
