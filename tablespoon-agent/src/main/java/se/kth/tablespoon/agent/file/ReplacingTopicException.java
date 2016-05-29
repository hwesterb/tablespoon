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
public class ReplacingTopicException extends Exception {

  public ReplacingTopicException(String replacesTopicId) {
    super(replacesTopicId + " was not found and could therefore not be replaced.");
  }
  
}