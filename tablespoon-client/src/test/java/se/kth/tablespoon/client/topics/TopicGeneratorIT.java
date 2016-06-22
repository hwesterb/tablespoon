package se.kth.tablespoon.client.topics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.BeforeClass;
import org.junit.Test;
import se.kth.tablespoon.client.events.EventType;
import se.kth.tablespoon.client.general.Group;
import se.kth.tablespoon.client.util.Time;


public class TopicGeneratorIT {
  
  private final String DIRECTORY = "/home/henke/generatedTopics/TOPICS_10_SEC_SEND";
  private final String TOPIC_ID = "testTopic";
  static Topic topic;
  static Group group;
  
  @BeforeClass
  public static void setUpClass() throws IOException {
    group = new Group("A");
    group.addMachine("1");
  }
  
  @Test
  public void test() throws IOException {
    int nbrOfTopics = 68;
    for (int i = 0; i < nbrOfTopics; i++) {
      String topicId = TOPIC_ID + i;
      topic = new GroupTopic(
          i,
          Time.now(),
          topicId,
          EventType.REGULAR,
          10,
          group
      );
      topic.generateJson();
      
      String fileName = DIRECTORY + topicId + ".json";
      File file = new File(fileName);
      // if file doesnt exists, then create it
      if (!file.exists()) {
        file.createNewFile();
      }
      FileWriter fw = new FileWriter(file.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(topic.getJson());
      bw.close();
    }
    
  }
  
  
}
