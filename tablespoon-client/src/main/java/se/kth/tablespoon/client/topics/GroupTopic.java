package se.kth.tablespoon.client.topics;

import java.util.HashSet;
import java.util.Set;
import se.kth.tablespoon.client.events.EventType;
import se.kth.tablespoon.client.general.Group;
import se.kth.tablespoon.client.general.Groups;

public final class GroupTopic extends Topic {
  
  Group group;
  
  public GroupTopic(int collectIndex, long startTime, String uniqueId, EventType type, int sendRate, Group group) {
    super(collectIndex, startTime, uniqueId, type, sendRate, group.getGroupId());
    this.group = group;
  }
  
  @Override
  public void updateMachineState(Groups groups) {
    // This happens automatically as getMachines are bound to group getMachines.
  }
  
  @Override
  public HashSet<String> getMachinesToNotify() {
    HashSet<String> machinesToNotify = new HashSet<>();
    for (String machine : group.getMachines()) {
      if (!machinesNotified.contains(machine)) {
        machinesToNotify.add(machine);
      }
    }
    return machinesToNotify;
  }
  
  @Override
  public boolean hasNoLiveMachines() {
    return group.getMachines().isEmpty();
  }
  
  @Override
  public Set<String> getMachines() {
    return group.getMachines();
  }
  
}
