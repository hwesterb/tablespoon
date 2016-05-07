/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.client.topics;

/**
 *
 * @author henke
 */
public class MissingTopicException extends Exception {

  /**
   * Creates a new instance of <code>MissingTopicException</code> without detail
   * message.
   */
  public MissingTopicException() {
    super("Accessing a topic which is not in the TopicStorage.");
  }

  /**
   * Constructs an instance of <code>MissingTopicException</code> with the
   * specified detail message.
   *
   * @param msg the detail message.
   */
  public MissingTopicException(String msg) {
    super(msg);
  }
}
