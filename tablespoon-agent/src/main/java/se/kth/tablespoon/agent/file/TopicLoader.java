/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.agent.file;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import se.kth.tablespoon.agent.events.Configuration;
import se.kth.tablespoon.agent.events.Topic;
import se.kth.tablespoon.agent.events.Topics;

public class TopicLoader extends FileLoader {
  
  private final String TOPICS_DIRECTORY = "topics";
  private final Configuration config = Configuration.getInstance();
  private final Topics topics;
  
  public TopicLoader(Topics topics) {
    this.topics = topics;
  }
  
  public void readTopicFiles() {
    List<String> list;
    try {
      list = listFilesInDirectory(TOPICS_DIRECTORY);
    } catch (IOException ex) {
      slf4jLogger.debug("Could not read directory: " + TOPICS_DIRECTORY + "."
          + " Attached message: " + ex.getMessage());
      return;
    }
    for (String fileName : list) {
      try {
        String uniqueId = findUniqueId(fileName);
        updateOrCreate(uniqueId, TOPICS_DIRECTORY, fileName);
      } catch (TopicAlreadyExistsException | WrongFileNameFormatException | JsonException | IOException ex) {
        slf4jLogger.debug(ex.getMessage());
      }
      deleteFile(TOPICS_DIRECTORY, fileName);
    }
  }
  
  private void updateOrCreate(String uniqueId, String directory, String fileName)
      throws TopicAlreadyExistsException, IOException, JsonException {
    Topic topic = topics.findTopic(uniqueId);
    if (topic==null) {
      createTopic(directory, fileName);
    }
    else throw new TopicAlreadyExistsException();
  }
  
  private void createTopic(String directory, String fileName) throws IOException, JsonException {
    Topic topic = new Topic();
    String json = loadJsonFromFile(directory, fileName);
    topic.interpretJson(json);
    if (topic.getReplacesTopicId() != null) {
      try {
        topics.replace(topic.getUniqueId(), topic.getReplacesTopicId());
      } catch (ReplacingTopicException ex) {
       slf4jLogger.debug(ex.getMessage());
      }
    }
    topics.addTopic(topic);
  }
  
  private String findUniqueId(String fileName) throws WrongFileNameFormatException {
    Pattern pattern = Pattern.compile("(.+).json");
    Matcher matcher = pattern.matcher(fileName);
    while (matcher.find()) {
      if (matcher.groupCount() == 1) {
        String uniqueId = matcher.group(1);
        return uniqueId;
      }
    }
    throw new WrongFileNameFormatException(fileName);
  }
  
  
}
