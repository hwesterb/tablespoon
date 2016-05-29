/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.client.general;

import java.util.HashSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author henke
 */
public class Group {
  

  public final Lock reentrantLock = new ReentrantLock();
  private final HashSet<String> machines = new HashSet<>();
  private final String groupId;

  public Group(String groupId) {
    this.groupId = groupId;
  }
  
  public void addMachine(String machine) {
    lock();
    machines.add(machine);
    unlock();
  }
  
    
  public void removeMachine(String machine) {
    lock();
    machines.remove(machine);
    unlock();
  }
  
  public void clear() {
    lock();
    machines.clear();
    unlock();
  }
  
  
  public void lock() {
    this.reentrantLock.lock();
  }
  
  public void unlock() {
    this.reentrantLock.unlock();
  }
  
  public HashSet<String> getMachines() {
    return machines;
  }

  public String getGroupId() {
    return groupId;
  }

  @Override
  public String toString() {
    return "Group{" + "reentrantLock=" + reentrantLock + ", machines=" + machines + ", groupId=" + groupId + '}';
  }
  
  
  
  
}
