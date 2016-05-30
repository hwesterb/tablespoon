package se.kth.tablespoon.agent.file;

public class ReplacingTopicException extends Exception {

  public ReplacingTopicException(String replacesTopicId) {
    super(replacesTopicId + " was not found and could therefore not be replaced.");
  }
  
}
