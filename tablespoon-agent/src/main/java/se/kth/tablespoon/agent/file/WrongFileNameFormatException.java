package se.kth.tablespoon.agent.file;

public class WrongFileNameFormatException extends Exception {

  public WrongFileNameFormatException(String fileName) {
    super("A topic file \"" + fileName  + "\" has the wrong format."
        + " It should have the format: uniqueId_version.json.");
  }
  
}
