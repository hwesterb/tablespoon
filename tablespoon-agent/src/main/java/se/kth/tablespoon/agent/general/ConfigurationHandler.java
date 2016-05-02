/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.agent.general;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.agent.listeners.CollectlListener;

/**
 *
 * @author henke
 */
public class ConfigurationHandler {
  
  private Configuration config;
  private static boolean hasLoadedConfig = false;
  private CollectlListener collectlListener;
  private final static Logger slf4jLogger = LoggerFactory.getLogger(ConfigurationHandler.class);
  
  public void loadNewConfiguration() {
    try {
      load();
      // If configuration has been loaded before it needs to be reconfigured.
      if (hasLoadedConfig) reconfigure();
      hasLoadedConfig = true;
    } catch (MissingConfigurationException e) {
      slf4jLogger.error(e.getMessage());
      if (hasLoadedConfig) slf4jLogger.debug("Configuration could not be"
          + " loaded, continues with old configuration.");
      else {
        slf4jLogger.debug("Configuration could not be loaded, exiting agent.");
        System.exit(0);
      }
    }
  }
  
  
  private void load() throws MissingConfigurationException {
    try {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      InputStream is = loader.getResourceAsStream("configuration/config.json");
      try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
        Gson gson = new Gson();
        config = gson.fromJson(br, Configuration.class);
        br.close();
      }
    } catch (NullPointerException | IOException e) {
      throw new MissingConfigurationException("The configuration file could not be located.");
    }
  }
  
  
  public void setCollectlListener(CollectlListener collectlListener) {
    this.collectlListener = collectlListener;
  }
  
  
  private void reconfigure() {
    if (config.isCollectlRestart()) {
      collectlListener.requestRestart();
    }
  }
  
  public Configuration getConfig() {
    return config;
  }
  
}