package se.kth.tablespoon.agent.main;

import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;
import se.kth.tablespoon.agent.file.FileWriter;
import se.kth.tablespoon.agent.file.JsonGenerator;
import se.kth.tablespoon.agent.util.Time;

public class StartIT {
  
  
  @Test
  public void testMain() throws IOException {
    (new Thread(new MainThread())).start();
    String uniqueId1 = "1.json";
    String uniqueId2 = "2.json";
    String uniqueId3 = "3.json";
    String json1 = JsonGenerator.someJson(0, uniqueId1, 4, 4, "");
    String json2 = JsonGenerator.someJson(1, uniqueId2, 4, 4, "");
    String json3 = JsonGenerator.someJson(0, uniqueId3, 4, 4, "");
    FileWriter.write(json1, "topics", uniqueId1);
    FileWriter.write(json2, "topics", uniqueId2);
    Time.sleep(9000);
    assertEquals(Start.agent.getSentEvents(), 2);
    printSentEvents();
    FileWriter.write(json3, "topics", uniqueId3);
    Time.sleep(8000);
    printSentEvents();
    assertEquals(Start.agent.getSentEvents(), 3);
  }
  
  private void printSentEvents() {
    System.out.println("Sent events: " + Start.agent.getSentEvents());
  }
  
  class MainThread implements Runnable {
    
    @Override
    public void run() {
      String[] args = null;
      Start.main(args);
    }
    
  }
  
}
