/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.events;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author henke
 */
public class EventRegistryTest {
  
  /**
   * Test of add method, of class EventRegistry.
   */
  @Test
  public void testAdd() {
    System.out.println("add");
    EventRegistry registry = new EventRegistry();
    EventDefinition event = EventFactory.create(registry, 2);
    registry.add(event);
    assertFalse(registry.isEmpty());
  }
  
  /**
   * Test of clean method, of class EventRegistry.
   */
  @Test
  public void testClean() {
    System.out.println("clean");
    ArrayList<String> liveMachines = new ArrayList<>();
    liveMachines.add("2");
    liveMachines.add("3");
    EventRegistry registry = new EventRegistry();
    EventDefinition ed = EventFactory.create(registry, 0);
    ed.addMachine("1");
    registry.add(ed);
    assertFalse(registry.isEmpty());
    registry.clean(liveMachines);
    assertTrue(registry.isEmpty());
  }
  
}
