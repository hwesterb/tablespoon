package se.kth.tablespoon.event.processing.json;

import java.util.Arrays;

public class RiemannEvent {
	public String host;
	public String service;
	public String state;
	public String description;
	public double metric;
	public String[] tags;
	public double time;
	public double ttl;
	
	@Override
	public String toString() {
		return "RiemannEvent [host=" + host + ", service=" + service + ", state=" + state + ", description="
				+ description + ", metric=" + metric + ", tags=" + Arrays.toString(tags) + ", time=" + time + ", ttl="
				+ ttl + "]";
	}
	
	
}
