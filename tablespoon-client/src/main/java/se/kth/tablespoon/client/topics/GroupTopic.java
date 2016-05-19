/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.client.topics;

import se.kth.tablespoon.client.events.EventType;
import java.util.ArrayList;
import se.kth.tablespoon.client.general.Group;
import se.kth.tablespoon.client.general.Groups;

/**
 *
 * @author henke
 */
public class GroupTopic extends Topic {

  public GroupTopic(int index, long startTime, String uniqueId, EventType type, Group group) {
    super(index, startTime, uniqueId, type, group.getMachines(), group.getGroupId());
  }

  @Override
  public void removeDeadMachines(Groups groups) {
    // This happens automatically as machines are bound to group machines.
  }
  
}
