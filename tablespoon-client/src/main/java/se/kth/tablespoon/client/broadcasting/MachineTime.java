/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.broadcasting;

import java.util.Objects;

/**
 *
 * @author henke
 */
class MachineTime {
  
  public final String machineId;
  public final long timeStamp;
  
  MachineTime(String machineId, long timeStamp) {
    this.machineId = machineId;
    this.timeStamp = timeStamp;
  }
  
  @Override
  public boolean equals(Object other){
    if (other == null) return false;
    if (other == this) return true;
    if (!(other instanceof MachineTime)) return false;
    MachineTime mt = (MachineTime) other;
    return mt.machineId.equals(machineId) && mt.timeStamp == timeStamp;
  }
  
  @Override
  public int hashCode() {
    int hash = 3;
    hash = 89 * hash + Objects.hashCode(this.machineId);
    hash = 89 * hash + (int) (this.timeStamp ^ (this.timeStamp >>> 32));
    return hash;
  }
  
}
