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
public class Groups implements Iterable<String> {
  
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
    for (String machine : this) {
      machineSnapshot.add(machine);
    }
  }
  
  public void retainWithSnapshot(ArrayList<String> machines) {
    machines.retainAll(machineSnapshot);
  }
  
  @Override
  public Iterator<String> iterator() {
    return new MachineIterator(groups);
  }
  
  private class MachineIterator implements Iterator<String> {
    
    private final Iterator<Group> groupIterator;
    private Iterator<String> machineIterator;
    
    public MachineIterator(TreeMap<String, Group> groups) {
      groupIterator = groups.values().iterator();
      
    }
    
    private boolean hasNextGroup() {
      machineIterator = groupIterator.next().getMachines().iterator();
      if (machineIterator.hasNext()) return true;
      else return false;
    }
    
    @Override
    public boolean hasNext() {
      if (machineIterator!=null && machineIterator.hasNext()) {
        return true;
      }
      if (groupIterator.hasNext() && machineIterator==null) {
        return hasNextGroup();
      } else if (groupIterator.hasNext() && !machineIterator.hasNext()) {
        return hasNextGroup();
      }
      return false;
    }
    
    @Override
    public String next() {
      return machineIterator.next();
    }
    
    @Override
    public void remove() {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }
  
  
}
