package se.kth.tablespoon.client.api;

public class MissingParameterException extends Exception {
  
  public MissingParameterException(String parameterName) {
    super("The " + parameterName + " needs to be specified.");
  }
  
}
