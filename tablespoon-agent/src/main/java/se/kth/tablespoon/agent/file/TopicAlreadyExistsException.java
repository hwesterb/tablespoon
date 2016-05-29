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
public class TopicAlreadyExistsException extends Exception {

  /**
   * Creates a new instance of <code>WrongFileNameFormatException</code> without
   * detail message.
   */
  public TopicAlreadyExistsException() {
    super("Attempting to load a topic that already exists.");
  }

}
