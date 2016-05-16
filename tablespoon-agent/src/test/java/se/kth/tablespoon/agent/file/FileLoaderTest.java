/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.agent.file;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import se.kth.tablespoon.agent.events.Configuration;
import se.kth.tablespoon.agent.events.Topic;

/**
 *
 * @author henke
 */
public class FileLoaderTest {
  
  private void writeNewFile(String json, String directory, String fileName) throws IOException {
    FileWriter fw = new FileWriter(FileLoader.class.getClassLoader().getResource(directory).getPath() + "/" + fileName);
    IOUtils.write(json,fw);
    IOUtils.closeQuietly(fw);
  }
  
  private String someJson(String uniqueId, int version, int duration, double threshold) {    
    String json = "{\"index\":0,\"version\":" + version 
        + ",\"startTime\":1463246537,\"uniqueId\": \"" + uniqueId + "\",\"groupId\":\"not specified\",\"type\":\"REGULAR\",\"duration\":" + duration 
        + ",\"sendRate\":\"NORMAL\",\"high\":{\"percentage\":" + threshold + ",\"comparator\":\"GREATER_THAN\"},\"low\":{\"percentage\":0.1,\"comparator\":\"LESS_THAN\"}}";
    return json;
  }
  
  private void generateJsonAndWrite(String file, int version, double threshold) throws IOException {
    writeNewFile(someJson(file, version, 12, threshold), "topics", file + "_" + version + ".json" );
  }
  
  @Test
  public void test1() throws IOException {
    
    (new ConfigurationLoader()).readConfigFile();
    Configuration config = Configuration.getInstance();
    
   
    String fileA = "uniqueIdA";
    int versionA = 1;
    generateJsonAndWrite(fileA, versionA, 0.3);
    TopicLoader tl = new TopicLoader();
    tl.readTopicFiles();
    assertEquals(fileA, config.findTopic(fileA).getUniqueId());
    
    generateJsonAndWrite(fileA, versionA, 0.4);
    tl.readTopicFiles();
    assertNotEquals(0.4, config.findTopic(fileA).getHigh().percentage, 0.01);
   
    versionA = 2;
    generateJsonAndWrite(fileA, versionA, 0.4);
    tl.readTopicFiles();
    assertEquals(0.4, config.findTopic(fileA).getHigh().percentage, 0.01);
  }
  
  
  @Test
  public void test2() throws IOException, OldTopicException {
    TopicLoader fl = new TopicLoader();
    Map<String, Topic> topics = new TreeMap<>();
    String directory = "topics";
    String fileName = "pqowiepoqwkepoqkwens120392js_1.json";
    String jsonIn = "{\"index\" : 0,}\"";
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
