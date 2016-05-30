package se.kth.tablespoon.agent.file;

import se.kth.tablespoon.agent.events.Configuration;
import java.io.IOException;

public class ConfigurationLoader extends FileLoader {
  
  private final String CONFIG_DIRECTORY = "configuration";
  private final String CONFIG_FILE_NAME = "config.json";
  
  public void readConfigFile() throws JsonException, IOException {
    Configuration config = Configuration.getInstance();
    String json = loadJsonFromFile(CONFIG_DIRECTORY, CONFIG_FILE_NAME);
    config.interpretJson(json);
  }
  
 
}