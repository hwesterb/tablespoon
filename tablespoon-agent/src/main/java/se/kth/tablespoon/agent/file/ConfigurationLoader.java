/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.agent.file;

import se.kth.tablespoon.agent.events.Configuration;
import java.io.IOException;

/**
 *
 * @author henke
 */
public class ConfigurationLoader extends FileLoader {
  
  private final String CONFIG_DIRECTORY = "configuration";
  private final String CONFIG_FILE_NAME = "config.json";
  
  public void readConfigFile() throws JsonException, IOException {
    Configuration config = Configuration.getInstance();
    String json = loadJsonFromFile(CONFIG_DIRECTORY, CONFIG_FILE_NAME);
    config.interpretJson(json);
  }
  
 
}