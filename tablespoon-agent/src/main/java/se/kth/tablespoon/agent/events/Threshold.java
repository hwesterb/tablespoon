/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.agent.events;

/**
 *
 * @author henke
 */
public class Threshold {
  
  public final double percentage;
  public final Comparator comparator;

  public Threshold(double percentage, Comparator comparator) {
    this.percentage = percentage;
    this.comparator = comparator;
  }
  
  
}
