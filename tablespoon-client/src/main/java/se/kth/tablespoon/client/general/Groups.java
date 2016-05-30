package se.kth.tablespoon.client.general;


import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Groups {
  
  private final ConcurrentHashMap<String, Group> groups = new ConcurrentHashMap<>();
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
    for (Group group : groups.values()) {
      machineSnapshot.addAll(group.getMachines());
    }
  }
  
  public void retainWithSnapshot(Set<String> machines) {
    machines.retainAll(machineSnapshot);
  }
  
}

