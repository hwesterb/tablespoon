package se.kth.tablespoon.client.broadcasting;

import org.junit.Test;
import se.kth.tablespoon.client.general.Groups;
import se.kth.tablespoon.client.topics.TopicStorage;

import java.io.IOException;

import static org.junit.Assert.*;

public class AgentBroadcasterAssistantTest {

    Groups groups = new Groups();
    TopicStorage storage = new TopicStorage(groups);
    AgentBroadcasterAssistant aba = new AgentBroadcasterAssistant(storage);

    @Test
    public void getAgentConfigTest() {
        try {
            System.out.println(aba.getAgentConfig("54.123.55.124", 5557));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
