package se.kth.tablespoon.agent.json;

public class AgentMessage {
	public String name = "hej";
	public boolean changed = false;
	
	@Override
	public String toString() {
		return "Collectl [name=" + name + ", changed=" + changed + "]";
	}
}
