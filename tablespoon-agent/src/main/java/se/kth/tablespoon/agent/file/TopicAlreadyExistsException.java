package se.kth.tablespoon.agent.file;

public class TopicAlreadyExistsException extends Exception {

  public TopicAlreadyExistsException() {
    super("Attempting to load a topic that already exists.");
  }

}
