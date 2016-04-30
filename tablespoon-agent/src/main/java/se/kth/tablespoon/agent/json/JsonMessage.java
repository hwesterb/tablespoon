package se.kth.tablespoon.agent.json;



public class JsonMessage {

	public CollectlMessage collectlMessage = new CollectlMessage();
	public AgentMessage agentMessage = new AgentMessage();
	
	
	
	@Override
	public String toString() {
		return "JsonMessage [collectlMessage=" + collectlMessage + ", agentMessage=" + agentMessage + "]";
	}

}
