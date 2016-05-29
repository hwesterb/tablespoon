/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.topics;

import java.util.HashSet;
import se.kth.tablespoon.client.events.EventType;
import se.kth.tablespoon.client.general.Group;
import se.kth.tablespoon.client.general.Groups;

/**
 *
 * @author henke
 */
public class GroupTopic extends Topic {
  
  Group group;
  
  public GroupTopic(int index, long startTime, String uniqueId, EventType type, int sendRate, Group group) {
    super(index, startTime, uniqueId, type, sendRate, group.getGroupId());
    this.group = group;
  }
  
  @Override
  public void updateMachineState(Groups groups) {
    // This happens automatically as machines are bound to group machines.
  }
  
  @Override
  public HashSet<String> getMachinesToNotify() {
    HashSet<String> machinesToNotify = new HashSet<>();
    group.lock();
    for (String machine : group.getMachines()) {
      if (!machinesNotified.contains(machine)) {
        machinesToNotify.add(machine);
      }
    }
    group.unlock();
    return machinesToNotify;
  }
  
  @Override
  public boolean hasNoLiveMachines() {
    group.lock();
    boolean state =  group.getMachines().isEmpty();
    group.unlock();
    return state;
  }

  @Override
  public HashSet<String> getInitialMachines() {
    // This is function is needed when replicating a MachineTopic, not a GroupTopic.
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
}
