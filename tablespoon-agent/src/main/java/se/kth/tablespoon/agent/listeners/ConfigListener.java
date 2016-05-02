package se.kth.tablespoon.agent.listeners;

import java.io.File;
import java.io.IOException;
import se.kth.tablespoon.agent.general.ConfigurationHandler;

public class ConfigListener implements Runnable {
  
  private boolean interruptRequest = false;
  private final ConfigurationHandler configurationHandler;
  
  public ConfigListener(ConfigurationHandler configurationHandler) {
    this.configurationHandler = configurationHandler;
  }
  
  public void look() throws IOException {
    while (true) {
      if (new File("configuration/config.properties").listFiles().length > 1) {
        configurationHandler.loadNewConfiguration();
      }
      if (interruptRequest) break;
    }
  }
  
  @Override
  public void run() {
    try {
      look();
    } catch (IOException e) {
      System.out.println("Exception caught: " + e.getMessage() + ".");
    }
    System.out.println("Ending looking-thread.");
  }
  
  //this is called from another thread
  public void requestInterrupt()  {
    //the other thread requests interrupt
    interruptRequest = true;
  }
  
  
}