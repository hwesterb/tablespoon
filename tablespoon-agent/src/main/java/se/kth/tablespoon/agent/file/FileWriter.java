package se.kth.tablespoon.agent.file;

import java.io.IOException;
import org.apache.commons.io.IOUtils;


public class FileWriter {
 
  public static void write(String str, String directory, String fileName) throws IOException {
    java.io.FileWriter fw = new java.io.FileWriter(FileLoader.class.getClassLoader().getResource(directory).getPath() + "/" + fileName);
    IOUtils.write(str,fw);
    IOUtils.closeQuietly(fw);
  }
  
  
}
