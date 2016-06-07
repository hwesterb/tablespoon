package se.kth.tablespoon.agent.file;

import com.fasterxml.jackson.jr.ob.JSON;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class  FileLoader {
  
  final Logger slf4jLogger = LoggerFactory.getLogger(FileLoader.class);
  
  protected List<String> listFilesInDirectory(String directory) throws IOException {
    List<String> files = IOUtils.readLines(getBuffer(directory + "/"), Charsets.UTF_8);
    return files;
  }
  
  protected BufferedInputStream getBuffer(String resource) {
    return IOUtils.buffer(FileLoader.class.getClassLoader()
        .getResourceAsStream(resource));
  }
  
  public String getJsonAndDelete(String directory, String fileName) throws IOException {
    String json = loadJsonFromFile(directory, fileName);
    deleteFile(getFile(directory, fileName));
    return json;
  }
  
  protected String loadJsonFromFile(String directory, String file) throws IOException {
    BufferedInputStream in = getBuffer(directory + "/" + file);
    byte[] contents = new byte[1024];
    int bytesRead = 0;
    String json = null;
    while( (bytesRead = in.read(contents)) != -1){
      json = new String(contents, 0, bytesRead);
    }
    in.close();
    return json;
  }
  
  protected void deleteFile(String directory, String fileName) {
    deleteFile(getFile(directory, fileName));
  }
  
  protected File getFile(String directory, String file) {
    return new File(FileLoader.class.getClassLoader().getResource(directory + "/" + file).getPath());
  }
  
  protected void deleteFile(File file) {
    if(file.delete()) {
      slf4jLogger.info(file.getName() + " was deleted.");
    } else {
      slf4jLogger.debug("Could not delete file " + file.getName() + ".");
    }
  }
  
  protected Object convertJsonToObject(String json, Object object) throws IOException {
    Object o = JSON.std.beanFrom(object.getClass(), json);
    return o;
  }
  
}
