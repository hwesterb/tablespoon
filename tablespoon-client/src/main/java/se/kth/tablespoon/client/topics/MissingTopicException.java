package se.kth.tablespoon.client.topics;

public class MissingTopicException extends Exception {

  public MissingTopicException() {
    super("Accessing a topic which is not in the TopicStorage.");
  }
  
}
