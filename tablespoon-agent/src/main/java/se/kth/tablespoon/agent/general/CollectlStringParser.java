package se.kth.tablespoon.agent.general;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.kth.tablespoon.agent.metrics.MetricLayout;
import se.kth.tablespoon.agent.metrics.MetricSource;
import se.kth.tablespoon.agent.metrics.MetricFormat;

//Parses input from collectl.
public class CollectlStringParser {
  
  // The delimiter is defined by collectl.
  private static final String DELIMETER = " ";
  
  public static String[] split(String str) {
    return str.split(DELIMETER);
  }
  
  // Splits headers from collectl and creating EventLayout objects.
  // The order of these objects specifies the order of metrics coming
  // from collectl.
  public static MetricLayout[] handleHeaders(String unsplitHeader) {
    //remove '#UTC', thus this is not an event, only a time stamp.
    unsplitHeader = unsplitHeader.substring(5, unsplitHeader.length());
    String[] headers = CollectlStringParser.split(unsplitHeader);
    MetricLayout[] mls = new MetricLayout[headers.length];
    for (int i = 0; i < headers.length; i++) {
      mls[i] = findHeader(headers[i]);
    }
    return mls;
  }
  
  // Collectl output has the same name as enumerators,
  // therefore the conversion can happen automatically.
  public static MetricSource findType(String str) {
    for (MetricSource source : MetricSource.values()) {
      if (str.equalsIgnoreCase(source.name())) return source;
    }
    return null;
  }
  
  private static MetricLayout findHeader(String header) {
    // Collectl does not have the same name as these enumerators,
    // therefore the conversion must be made manually.
    FormatRegex[] frs = {
      new FormatRegex(MetricFormat.PERSEC, "/sec"),
      new FormatRegex(MetricFormat.TOTAL, "Tot"),
      new FormatRegex(MetricFormat.TOTAL, "Total"),
      new FormatRegex(MetricFormat.PERCENTAGE, "%"),
      new FormatRegex(MetricFormat.AVG1, "Avg1"),
      new FormatRegex(MetricFormat.AVG5, "Avg5"),
      new FormatRegex(MetricFormat.AVG15, "Avg15"),
      new FormatRegex(MetricFormat.QUE, "Que"),
      new FormatRegex(MetricFormat.RUN, "Run"),
      new FormatRegex(MetricFormat.FAULTS, "Faults")
    };
    
    String thirdGroup = "(";
    for (int i = 0; i < frs.length-1; i++) {
      thirdGroup += frs[i].getRegex() + "|";
    }
    thirdGroup += frs[frs.length-1].getRegex();
    thirdGroup += ")";
    String twoGroups = "\\[([A-Z]+)\\](.+)";
    
    // The first part can be matched because it has brackets.
    // The last part however must be defined by it's name,
    // these names are defined in the FormatRegex array.
    Pattern pattern = Pattern.compile(twoGroups + thirdGroup);
    Matcher matcher = pattern.matcher(header);
    MetricLayout ml = new MetricLayout();
    boolean matched = false;
    while (matcher.find()) {
      if (matcher.groupCount() == 3) {
        matched = true;
        //Example:  [CPU]User%
        //group 1 type "CPU"
        ml.setSource(findType(matcher.group(1)));
        //group 2 name "User"
        ml.setName(matcher.group(2));
        //group 3 format "%"
        for (FormatRegex fr : frs) {
          if (matcher.group(3).contentEquals(fr.getRegex())) ml.setFormat(fr.getFormat());
        }
        break;
      }
    }
    if (!matched) {
      pattern = Pattern.compile(twoGroups);
      matcher = pattern.matcher(header);
      while (matcher.find()) {
        if (matcher.groupCount() == 2) {
          ml.setSource(findType(matcher.group(1)));
          ml.setName(matcher.group(2));
          ml.setFormat(MetricFormat.UNITS);
          break;
        }
      }
    }
    
    return ml;
  }
  
  private static class FormatRegex {
    
    private final MetricFormat format;
    private final String regex;
    
    public FormatRegex(MetricFormat format, String regex) {
      this.format = format;
      this.regex = regex;
    }
    
    public String getRegex() {
      return regex;
    }
    
    public MetricFormat getFormat() {
      return format;
    }
  }
}



