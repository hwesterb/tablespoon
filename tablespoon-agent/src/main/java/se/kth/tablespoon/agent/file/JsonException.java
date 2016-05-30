package se.kth.tablespoon.agent.file;

public class JsonException extends Exception {

  public JsonException(String nameOfParameter) {
    super("The mandatory parameter " + nameOfParameter + " could not be interpreted.");
  }
}
