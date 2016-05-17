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
        handleTopic(TOPICS_DIRECTORY, fileName);
      } catch (OldTopicException | WrongFileNameFormatException | JsonException | IOException ex) {
        slf4jLogger.debug(ex.getMessage());
      }
      deleteFile(TOPICS_DIRECTORY, fileName);
    }
  }
  
  private String handleTopic(String directory, String fileName) throws OldTopicException, WrongFileNameFormatException, IOException, JsonException {
    Pattern pattern = Pattern.compile("(.+)_([0-9]+).json");
    Matcher matcher = pattern.matcher(fileName);
    while (matcher.find()) {
      if (matcher.groupCount() == 2) {
        String uniqueId = matcher.group(1);
        int version = Integer.parseInt(matcher.group(2));
        updateOrCreate(uniqueId, version, directory, fileName);
        return uniqueId;
      }
    }
    throw new WrongFileNameFormatException(fileName);
  }
  
  
  private void updateOrCreate(String uniqueId, int version, String directory, String fileName)
      throws OldTopicException, IOException, JsonException {
    Topic topic = topics.findTopic(uniqueId);
    if (topic==null) {
      createTopic(directory, fileName);
    }
    else {
      if (version > topic.getVersion()) updateTopic(topic, directory, fileName);
      else throw new OldTopicException();
    }
  }
  
  private void createTopic(String directory, String fileName) throws IOException, JsonException {
    Topic topic = new Topic();
    String json = loadJsonFromFile(directory, fileName);
    topic.interpretJson(json);
    topics.addTopic(topic);
  }
  
  private void updateTopic(Topic topic, String directory, String fileName) throws IOException, JsonException {
    String json = loadJsonFromFile(directory, fileName);
    topic.interpretJson(json);
  }
  
}
