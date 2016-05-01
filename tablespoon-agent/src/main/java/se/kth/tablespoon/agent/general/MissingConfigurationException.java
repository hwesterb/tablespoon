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
public class MissingConfigurationException extends Exception {

  public MissingConfigurationException(String msg) {
    super(msg);
  }
}
