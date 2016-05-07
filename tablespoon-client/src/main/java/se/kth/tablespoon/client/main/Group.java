/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.client.main;

import java.util.ArrayList;

/**
 *
 * @author henke
 */
public class Group {
  
  private final ArrayList<String> machines = new ArrayList<String>();
  private final String groupId;

  public Group(String groupId) {
    this.groupId = groupId;
  }
  
  public void addMachine(String machine) {
    machines.add(machine);
  }
  
  public ArrayList<String> getMachines() {
    return machines;
  }

  public String getGroupId() {
    return groupId;
  }
  
  
}
