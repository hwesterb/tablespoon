/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.topics;

import se.kth.tablespoon.client.events.EventType;
import java.util.HashSet;
import se.kth.tablespoon.client.general.Groups;

/**
 *
 * @author henke
 */
public class MachineTopic extends Topic {
  
  private final HashSet<String> initialMachines = new HashSet<>();
  private final HashSet<String> activeMachines = new HashSet<>();
  
  public MachineTopic(int index, long startTime, String uniqueId, EventType type, 
      int sendRate, HashSet<String> machines) {
    super(index, startTime, uniqueId, type, sendRate, "not specified");
    this.initialMachines.addAll(machines);
    this.activeMachines.addAll(machines);
  }
  
  @Override
  public void updateMachineState(Groups groups) {
    groups.retainWithSnapshot(activeMachines);
  }
  
  @Override
  public HashSet<String> getMachinesToNotify() {
    HashSet<String> machinesToNotify = new HashSet<>();
    for (String machine : activeMachines) {
      if (!machinesNotified.contains(machine)) {
        machinesToNotify.add(machine);
      }
    }
    return machinesToNotify;
  }
  
  @Override
  public boolean hasNoLiveMachines() {
    return activeMachines.isEmpty();
  }
  
  @Override
  public HashSet<String> getInitialMachines() {
    return initialMachines;
  }
  
  
}
