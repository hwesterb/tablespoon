/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.agent.general;

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
      config = new Configuration();
      // If configuration has been loaded before it needs to be reconfigured.
      if (hasLoadedConfig) reconfigure();
      hasLoadedConfig = true;
    } catch (MissingPropertyException |
        MissingConfigurationException |
        BooleanFormatException |
        NumberFormatException e) {
      slf4jLogger.error(e.getMessage());
      if (hasLoadedConfig) slf4jLogger.debug("Configuration could not be"
          + " loaded, continues with old configuration.");
      else {
        slf4jLogger.debug("Configuration could not be loaded, exiting agent.");
        System.exit(0);
      }
    }
  }

  public void setCollectlListener(CollectlListener collectlListener) {
    this.collectlListener = collectlListener;
  }
  
  
  public void reconfigure() {
    if (config.restartCollectl()) {
      collectlListener.restartCollectl();
    }
    
  }
  
  public Configuration getConfig() {
    return config;
  }
 
}
