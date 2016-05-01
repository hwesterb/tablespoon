package se.kth.tablespoon.agent.general;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.kth.tablespoon.agent.events.EventLayout;
import se.kth.tablespoon.agent.events.EventType;
import se.kth.tablespoon.agent.events.Format;

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
  public static EventLayout[] handleHeaders(String unsplitHeader) {
    //remove '#UTC', thus this is not an event, only a time stamp.
    unsplitHeader = unsplitHeader.substring(5, unsplitHeader.length());
    String[] headers = CollectlStringParser.split(unsplitHeader);
    EventLayout[] els = new EventLayout[headers.length];
    for (int i = 0; i < headers.length; i++) {
      els[i] = findHeader(headers[i]);
    }
    return els;
  }
  
  // Collectl output has the same name as enumerators,
  // therefore the conversion can happen automatically.
  public static EventType findType(String str) {
    for (EventType type : EventType.values()) {
      if (str.equalsIgnoreCase(type.name())) return type;
    }
    return null;
  }
  
  private static EventLayout findHeader(String header) {
    // Collectl does not have the same name as these enumerators,
    // therefore the conversion must be made manually.
    FormatRegex[] frs = {
      new FormatRegex(Format.PERSEC, "/sec"),
      new FormatRegex(Format.TOTAL, "Tot"),
      new FormatRegex(Format.PERCENTAGE, "%"),
      new FormatRegex(Format.AVG1, "Avg1"),
      new FormatRegex(Format.AVG5, "Avg5"),
      new FormatRegex(Format.AVG15, "Avg15"),
      new FormatRegex(Format.QUE, "Que"),
      new FormatRegex(Format.RUN, "Run")
    };
    
    String regexPart = "";
    for (int i = 0; i < frs.length-1; i++) {
      regexPart += frs[i].getRegex() + "|";
    }
    regexPart += frs[frs.length-1].getRegex();
    
    // The first part can be matched because it has brackets.
    // The last part however must be defined by it's name,
    // these names are defined in the FormatRegex array.
    Pattern pattern = Pattern.compile("\\[([A-Z]+)\\](.*)(" + regexPart + ")");
    Matcher matcher = pattern.matcher(header);
    EventLayout el = new EventLayout();
    while (matcher.find()) {
      if (matcher.groupCount() == 3) {
        //Example:  [CPU]User%
        //group 1 type "CPU"
        el.setType(findType(matcher.group(1)));
        //group 2 name "User"
        el.setName(matcher.group(2));
        //group 3 format "%"
        for (FormatRegex fr : frs) {
          if (matcher.group(3).contentEquals(fr.getRegex())) el.setFormat(fr.getFormat());
        }
        break;
      }
    }
    return el;
  }
  
  private static class FormatRegex {
    
    private final Format format;
    private final String regex;
    
    public FormatRegex(Format format, String regex) {
      this.format = format;
      this.regex = regex;
    }
    
    public String getRegex() {
      return regex;
    }
    
    public Format getFormat() {
      return format;
    }
  }
}



