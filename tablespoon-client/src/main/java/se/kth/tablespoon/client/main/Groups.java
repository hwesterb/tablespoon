/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.main;

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
    machineSnapshot.retainAll(machines);
  }
  
  @Override
  public Iterator<String> iterator() {
    return new MachineIterator(groups);
  }
  
  private class MachineIterator implements Iterator<String> {
    
    private final Iterator<Group> groupIterator;
    private Iterator<String> machineIterator;
    private boolean nextInnerLevel = false;
    
    public MachineIterator(TreeMap<String, Group> groups) {
      groupIterator = groups.values().iterator();
    }
    
    @Override
    public boolean hasNext() {
      if (machineIterator.hasNext()) {
        nextInnerLevel = true;
        return true;
      } else if (groupIterator.hasNext()) {
        nextInnerLevel = false;
        return true;
      }
      return false;
    }
    
    @Override
    public String next() {
      if (nextInnerLevel) return machineIterator.next();
      else {
        machineIterator = groupIterator.next().getMachines().iterator();
        return machineIterator.next();
      }
    }
    
    @Override
    public void remove() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    
  }
  
  
}
