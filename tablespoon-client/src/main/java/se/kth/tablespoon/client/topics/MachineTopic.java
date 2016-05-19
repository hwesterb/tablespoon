/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.client.topics;

import java.util.ArrayList;
import se.kth.tablespoon.client.general.Groups;

/**
 *
 * @author henke
 */
public class MachineTopic extends Topic {

  public MachineTopic(int index, long startTime, String uniqueId, EventType type) {
    super(index, startTime, uniqueId, type, new ArrayList<String>(), "not specified");
  }

  @Override
  public void removeDeadMachines(Groups groups) {
    groups.retainWithSnapshot(machines);
  }

  
}
