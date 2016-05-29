/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.general;

<<<<<<< Updated upstream
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
||||||| merged common ancestors
import java.util.ArrayList;
import java.util.TreeMap;
=======
import java.util.HashSet;
import java.util.HashMap;
>>>>>>> Stashed changes

/**
 *
 * @author henke
 */
public class Groups implements Iterable<String> {
  
  private final HashMap<String, Group> groups = new HashMap<>();
  private final HashSet<String> machineSnapshot = new HashSet<>();
  
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
    Group group = groups.get(groupId);
    group.clear();
    groups.remove(groupId);
  }
  
  public void takeSnapshop() {
    machineSnapshot.clear();
<<<<<<< Updated upstream
    for (String machine : this) {
      machineSnapshot.add(machine);
||||||| merged common ancestors
    for (Group group : groups.values()) {
      machineSnapshot.addAll(group.getMachines());
=======
    for (Group group : groups.values()) {
      group.lock();
      machineSnapshot.addAll(group.getMachines());
      group.unlock();
>>>>>>> Stashed changes
    }
  }
  
  public void retainWithSnapshot(HashSet<String> machines) {
    machines.retainAll(machineSnapshot);
  }
  
<<<<<<< Updated upstream
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
      return machineIterator.hasNext();
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
||||||| merged common ancestors
}
=======
  
}
>>>>>>> Stashed changes
