package se.kth.tablespoon.agent.file;

import java.io.IOException;

public class JsonGenerator {
  
  public static String someJson(int collectIndex, String uniqueId, int duration, int sendRate, String extra) {
    String json = "{\"collectIndex\":" +
        collectIndex + ",\"startTime\":1463246537,\"uniqueId\": \"" +
        uniqueId + "\",\"groupId\":\"not specified\",\"eventType\":\"REGULAR\",\"duration\":" +
        duration + ",\"sendRate\":" +
        sendRate + extra + "}";
    return json;
  }
  
  public static void generateJsonAndWrite(String file, String extra) throws IOException {
    FileWriter.write(someJson(0, file, 0, 0, extra), "topics", file + ".json" );
  }
  
  
}
