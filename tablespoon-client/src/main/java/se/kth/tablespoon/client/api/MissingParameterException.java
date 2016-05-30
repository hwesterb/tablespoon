package se.kth.tablespoon.client.api;

class MissingParameterException extends Exception {
  
  public MissingParameterException(String parameterName) {
    super("The " + parameterName + " needs to be specified.");
  }
  
}
