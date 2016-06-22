package se.kth.tablespoon.client.general;

import se.kth.tablespoon.client.api.TablespoonApi;
import se.kth.tablespoon.client.broadcasting.AgentBroadcasterAssistant;
import se.kth.tablespoon.client.broadcasting.AgentBroadcaster;
import se.kth.tablespoon.client.broadcasting.RiemannSubscriberBroadcaster;
import se.kth.tablespoon.client.topics.TopicStorage;

public class Start {
  
  
  public static void main(String args[]) {
     
  }
  
  public static TablespoonApi setUp(Groups groups, AgentBroadcaster broadcaster, String riemannHost, int riemannPort) {
    TopicStorage storage = new TopicStorage(groups);
    AgentBroadcasterAssistant aba = new AgentBroadcasterAssistant(storage);
    RiemannSubscriberBroadcaster rsb = new RiemannSubscriberBroadcaster(riemannHost, riemannPort, storage);
    TablespoonApi api = new TablespoonApi(storage, groups, rsb);
    Thread abaThread = new Thread(aba);
    abaThread.start();
    aba.registerBroadcaster(broadcaster);
    Thread sbThread = new Thread(rsb);
    sbThread.start();
    return api;
  }
  
}