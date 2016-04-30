package se.kth.tablespoon.agent.json;

public class CollectlMessage {
	
	public String name = "hej";
	public String config = "collectl -P -o U";
	public boolean changed = true;
	
	@Override
	public String toString() {
		return "Collectl [name=" + name + ", changed=" + changed + "]";
	}
	
}
