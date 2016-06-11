package se.kth.tablespoon.agent.file;

import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.io.IOUtils;


public class JsonWriter {
  
  
  public static void write(String json, String directory, String fileName) throws IOException {
    FileWriter fw = new FileWriter(FileLoader.class.getClassLoader().getResource(directory).getPath() + "/" + fileName);
    IOUtils.write(json,fw);
    IOUtils.closeQuietly(fw);
  }
  
  public static String someJson(int collectIndex, String uniqueId, int duration, int sendRate, double threshold) {
    String json = "{\"collectIndex\":" +
        collectIndex + ",\"startTime\":1463246537,\"uniqueId\": \"" +
        uniqueId + "\",\"groupId\":\"not specified\",\"eventType\":\"REGULAR\",\"duration\":" +
        duration + ",\"sendRate\":" +
        sendRate + ",\"high\":{\"percentage\":"
        + threshold + ",\"comparator\":\"GREATER_THAN\"},\"low\":{\"percentage\":10.0,\"comparator\":\"LESS_THAN\"}}";
    return json;
  }
  
  public static void generateJsonAndWrite(String file, double threshold) throws IOException {
    write(someJson(0, file, 0, 0, threshold), "topics", file + ".json" );
  }
  
  
}
