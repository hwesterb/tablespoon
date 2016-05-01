/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.agent.general;

/**
 *
 * @author henke
 */
public class MissingPropertyException extends Exception {

  public MissingPropertyException(String property) {
    super("Could not load property " + property + ".");
  }
  
}
