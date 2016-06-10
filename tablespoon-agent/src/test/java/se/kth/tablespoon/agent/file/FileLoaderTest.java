package se.kth.tablespoon.agent.file;

import java.io.IOException;
import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import se.kth.tablespoon.agent.events.Topics;

public class FileLoaderTest {
  
  @Test
  public void test1() throws IOException, JsonException {
    (new ConfigurationLoader()).readConfigFile();
    Topics topics = new Topics();
    String fileA = "uniqueIdA";
    
    JsonWriter.generateJsonAndWrite(fileA, 30.0);
    TopicLoader tl = new TopicLoader(topics);
    tl.readTopicFiles();
    assertEquals(fileA, topics.findTopic(fileA).getUniqueId());
    
    JsonWriter.generateJsonAndWrite(fileA, 40.0);
    tl.readTopicFiles();
    assertEquals(30.0, topics.findTopic(fileA).getHigh().percentage, 0.01); 
  }
  
  @Test
  public void test2() throws IOException, TopicAlreadyExistsException {
    Topics topics = new Topics();
    TopicLoader fl = new TopicLoader(topics);
    String directory = "topics";
    String fileName = "pqowiepoqwkepoqkwens120392js.json";
    String jsonIn = "{\"collectIndex\" : 0,}\"";
    JsonWriter.write(jsonIn, directory, fileName);
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
