/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.api;

/**
 *
 * @author henke
 */
class MissingParameterException extends Exception {
  
  public MissingParameterException(String parameterName) {
    super("The " + parameterName + " needs to be specified.");
  }
  
}
