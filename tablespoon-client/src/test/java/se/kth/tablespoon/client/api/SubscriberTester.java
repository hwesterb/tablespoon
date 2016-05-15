/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.client.api;

/**
 *
 * @author henke
 */
public class SubscriberTester implements Subscriber {

  private String uniqueId;
  
  
  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public String getUniqueId() {
    return uniqueId;
  }
  
  @Override
  public void onEventArrival(Event event) {
    System.out.println(event);
  }
  
}
