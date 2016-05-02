package se.kth.tablespoon.agent.metrics;

public class MetricLayout {

	private MetricSource source;
	private String name;
	private MetricFormat format;
	
	public MetricSource getSource() {
		return source;
	}
	public void setType(MetricSource source) {
		this.source = source;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public MetricFormat getFormat() {
		return format;
	}
	public void setFormat(MetricFormat format) {
		this.format = format;
	}

}
