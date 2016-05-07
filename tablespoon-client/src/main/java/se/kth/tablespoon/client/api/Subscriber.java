/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.client.api;

public interface Subscriber {

  public void setUniqueId(String uniqueId);
  public void onEventArrival(Event event);
  
}
