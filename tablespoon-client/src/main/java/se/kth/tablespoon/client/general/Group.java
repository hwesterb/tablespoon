/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.client.general;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author henke
 */
public class Group {
  
  private final Set<String> machines = 
      Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
  private final String groupId;

  public Group(String groupId) {
    this.groupId = groupId;
  }
  
  public void addMachine(String machine) {
    machines.add(machine);
  }
  
    
  public void removeMachine(String machine) {
    machines.remove(machine);
  }
  
  public void clear() {
    machines.clear();
  }
  
 
  public Set<String> getMachines() {
    return machines;
  }

  public String getGroupId() {
    return groupId;
  }
 
  
}
