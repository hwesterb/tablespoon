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
  private int counter = 0;
  private TablespoonEvent event;
  
  
  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public String getUniqueId() {
    return uniqueId;
  }
  
  @Override
  public void onEventArrival(TablespoonEvent event) {
    this.event = event;
    System.out.println(event);
    counter++;
  }

  public int getCounter() {
    return counter;
  }

  public TablespoonEvent getEvent() {
    return event;
  }

  
  
}
