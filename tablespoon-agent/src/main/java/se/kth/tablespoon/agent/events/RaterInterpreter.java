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
public class RaterInterpreter {
  
  public static double getNumber(Rate rate) {
    switch (rate) {
      case VERY_SLOW: return 10.0;
      case SLOW: return 5.0;
      case NORMAL: return 0.0;
      case FAST: return 0.5;
      case VERY_FAST: return 0.25;
    }
    return 0.0;
  }
  
  public static int sendWhenCounterIs(Rate sendRate, Rate collectionRate) {
    Double d = getNumber(sendRate)/getNumber(collectionRate);
    return d.intValue();
  }
  
  
}
