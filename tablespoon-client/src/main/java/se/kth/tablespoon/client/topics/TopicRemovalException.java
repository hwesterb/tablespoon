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
public class TopicRemovalException extends Exception {

  /**
   * Creates a new instance of <code>TopicRemovalException</code> without detail
   * message.
   */
  public TopicRemovalException() {
  }

  /**
   * Constructs an instance of <code>TopicRemovalException</code> with the
   * specified detail message.
   *
   * @param msg the detail message.
   */
  public TopicRemovalException(String msg) {
    super(msg);
  }
}
