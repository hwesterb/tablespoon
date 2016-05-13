package se.kth.tablespoon.agent.listeners;

import java.io.File;
import java.io.IOException;
import se.kth.tablespoon.agent.file.ConfigurationLoader;

public class ConfigListener implements Runnable {

  private boolean interruptRequest = false;
  private final ConfigurationLoader configurationHandler;

  public ConfigListener(ConfigurationLoader configurationHandler) {
    this.configurationHandler = configurationHandler;
  }

  public void look() throws IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    while (true) {
      if (new File(classLoader.getResource("configuration").getFile()).listFiles().length > 1) {
        configurationHandler.loadNewConfiguration();
      }
      if (interruptRequest) {
        break;
      }
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
  public void requestInterrupt() {
    //the other thread requests interrupt
    interruptRequest = true;
  }

}
