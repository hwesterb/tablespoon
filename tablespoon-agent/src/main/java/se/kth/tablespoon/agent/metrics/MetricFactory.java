/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.agent.metrics;

import java.util.ArrayList;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.agent.general.CollectlStringParser;
import se.kth.tablespoon.agent.general.Configuration;


public class MetricFactory {
  
  private final static Logger slf4jLogger = LoggerFactory.getLogger(MetricFactory.class);
  
  public static Metric[] createMetrics(String line, MetricLayout[] mls, Configuration config) {
    String[] rowArray = CollectlStringParser.split(line);
    long timeStamp = Math.round(Double.parseDouble(rowArray[0]));
    double[] numericRowArray =  new double[mls.length];
    // The first column is a time stamp, and shall be included in every
    // message. Therefore it should not be part of the numericRowArray.
    for (int i = 1; i <  rowArray.length; i++) {
      numericRowArray[i-1] = Double.parseDouble(rowArray[i]);
    }
    return match(numericRowArray, timeStamp, mls, config);
  }
  
  
  private static Metric[] match(double[] row, long timeStamp, MetricLayout[] mls, Configuration config) {
    ArrayList<Metric> metrics = new ArrayList<>();
    for (int i = 0; i < mls.length; i++) {
      Metric metric = new Metric(i, mls[i].getSource(), mls[i].getFormat(), timeStamp, mls[i].getName(), row[i]);
      metrics.add(metric);
    }
    return metrics.toArray(new Metric[metrics.size()]);
  }
  
}
