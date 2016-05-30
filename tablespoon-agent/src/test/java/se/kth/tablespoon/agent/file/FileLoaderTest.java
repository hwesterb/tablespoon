package se.kth.tablespoon.agent.file;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import se.kth.tablespoon.agent.events.Topics;

public class FileLoaderTest {
  
  private void writeNewFile(String json, String directory, String fileName) throws IOException {
    FileWriter fw = new FileWriter(FileLoader.class.getClassLoader().getResource(directory).getPath() + "/" + fileName);
    IOUtils.write(json,fw);
    IOUtils.closeQuietly(fw);
  }
  
  private String someJson(String uniqueId, int duration, double threshold) {    
    String json = "{\"collectIndex\":0,\"startTime\":1463246537,\"uniqueId\": \"" + uniqueId + "\",\"groupId\":\"not specified\",\"eventType\":\"REGULAR\",\"duration\":" + duration 
        + ",\"sendRate\":3,\"high\":{\"percentage\":" + threshold + ",\"comparator\":\"GREATER_THAN\"},\"low\":{\"percentage\":10.0,\"comparator\":\"LESS_THAN\"}}";
    return json;
  }
  
  private void generateJsonAndWrite(String file, double threshold) throws IOException {
    writeNewFile(someJson(file, 12, threshold), "topics", file + ".json" );
  }
  
  @Test
  public void test1() throws IOException, JsonException {
    (new ConfigurationLoader()).readConfigFile();
    Topics topics = new Topics();
    String fileA = "uniqueIdA";

    generateJsonAndWrite(fileA, 30.0);
    TopicLoader tl = new TopicLoader(topics);
    tl.readTopicFiles();
    assertEquals(fileA, topics.findTopic(fileA).getUniqueId());
    
    generateJsonAndWrite(fileA, 40.0);
    tl.readTopicFiles();
    assertEquals(30.0, topics.findTopic(fileA).getHigh().percentage, 0.01);
   
  }
  
  
  @Test
  public void test2() throws IOException, TopicAlreadyExistsException {
    Topics topics = new Topics();
    TopicLoader fl = new TopicLoader(topics);
    String directory = "topics";
    String fileName = "pqowiepoqwkepoqkwens120392js_1.json";
    String jsonIn = "{\"collectIndex\" : 0,}\"";
    writeNewFile(jsonIn, directory, fileName);
    List<String> list = fl.listFilesInDirectory(directory);
    System.out.println("Number of files found: " + list.size());
    String jsonOut = "";
    for (String fileNameFound : list) {
      if (fileNameFound.contentEquals(fileName)) {
        jsonOut = fl.getJsonAndDelete(directory, fileNameFound);
      }
    }
    Assert.assertEquals(jsonIn, jsonOut);
  }
  
}
