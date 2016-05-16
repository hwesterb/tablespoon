/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.agent.file;

/**
 *
 * @author henke
 */
public class TopicJsonException extends Exception {

  public TopicJsonException(String nameOfParameter) {
    super("The mandatory parameter " + nameOfParameter + " could not be interpreted.");
  }
}
