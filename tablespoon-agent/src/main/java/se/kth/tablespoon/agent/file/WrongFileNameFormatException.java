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
public class WrongFileNameFormatException extends Exception {

  public WrongFileNameFormatException(String fileName) {
    super("A topic file \"" + fileName  + "\" has the wrong format."
        + " It should have the format: uniqueId_version.json.");
  }

  
}
