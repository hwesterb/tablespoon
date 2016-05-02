package se.kth.tablespoon.agent.aggregation;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.riemann.Riemann;
import com.codahale.metrics.riemann.RiemannReporter;
import com.codahale.metrics.riemann.RiemannReporter.Builder;
import java.io.IOException;

public class MetricsHandler {
	
//	TODO: Use metrics to pre process messages before sending them.
	public void create(String host, int port) {
		MetricRegistry mr = new MetricRegistry();
        mr.meter("blabla");
        mr.histogram("hehe");
        
        Meter requests = mr.meter("requests");
        requests.mark();
        requests.mark();
        
    
		Builder b =  RiemannReporter.forRegistry(mr);
        b.filter(MetricFilter.ALL);
    
            
		Riemann r = null;
		try {
			r = new Riemann(host, port);
		} catch (IOException e) {
			System.out.println("Can't connect because: " + e.getMessage());
			System.exit(0);
		}
		RiemannReporter rr = b.build(r);
	}

	
	public Builder configureBuilder(MetricRegistry mr) {
		Builder b = RiemannReporter.forRegistry(mr);
	
		return b;
	}
}
