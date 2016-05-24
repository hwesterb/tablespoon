/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.general;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 *
 * @author henke
 */
public class Groups {
  
  private final TreeMap<String, Group> groups = new TreeMap<>();
  private final ArrayList<String> machineSnapshot = new ArrayList<>();
  
  public void addMachine(String groupId, String machine) {
    Group group = groups.get(groupId);
    group.addMachine(machine);
  }
  
  public void add(Group group) {
    groups.put(group.getGroupId(), group);
  }
  
  public Group get(String groupId) {
    return groups.get(groupId);
  }
  
  public void remove(String groupId) {
    groups.remove(groupId);
  }
  
  public void takeSnapshop() {
    machineSnapshot.clear();
    for (Group group : groups.values()) {
      machineSnapshot.addAll(group.getMachines());
    }
  }
  
  public void retainWithSnapshot(ArrayList<String> machines) {
    machines.retainAll(machineSnapshot);
  }
  
}