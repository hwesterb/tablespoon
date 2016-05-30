package se.kth.tablespoon.client.topics;

import java.util.Collections;
import se.kth.tablespoon.client.events.EventType;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import se.kth.tablespoon.client.general.Groups;

public class MachineTopic extends Topic {
  
  private final Set<String> machines = 
      Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
  
  public MachineTopic(int collectIndex, long startTime, String uniqueId, EventType type, 
      int sendRate, Set<String> machines) {
    super(collectIndex, startTime, uniqueId, type, sendRate, "not specified");
    this.machines.addAll(machines);
  }
  
  @Override
  public void updateMachineState(Groups groups) {
    groups.retainWithSnapshot(machines);
  }
  
  @Override
  public HashSet<String> getMachinesToNotify() {
    HashSet<String> machinesToNotify = new HashSet<>();
    for (String machine : machines) {
      if (!machinesNotified.contains(machine)) {
        machinesToNotify.add(machine);
      }
    }
    return machinesToNotify;
  }
  
  @Override
  public boolean hasNoLiveMachines() {
    return machines.isEmpty();
  }
  
  @Override
  public Set<String> getMachines() {
    return machines;
  }
  
  
}
