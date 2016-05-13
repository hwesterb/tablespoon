/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
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

/**
 *
 * @author henke
 */
public class FileLoader {
  
  final Logger slf4jLogger = LoggerFactory.getLogger(FileLoader.class);
  
  public List<String> listFilesInDirectory(String directory) throws IOException {
    List<String> files = IOUtils.readLines(FileLoader.class.getClassLoader()
        .getResourceAsStream(directory + "/"), Charsets.UTF_8);
    return files;
  }
  
  private BufferedInputStream getBuffer(String directory, String file) {
    return IOUtils.buffer(FileLoader.class.getClassLoader()
        .getResourceAsStream(directory + "/" + file));
  }
  
  public String loadJsonFromFile(String directory, String file) throws IOException {
    BufferedInputStream in = getBuffer(directory, file);
    byte[] contents = new byte[1024];
    int bytesRead = 0;
    String json = null;
    while( (bytesRead = in.read(contents)) != -1){
      json = new String(contents, 0, bytesRead);
    }
    in.close();
    return json;
  }
  
  public File getFile(String directory, String file) {
    return new File(FileLoader.class.getResource(directory + "/" + file).getPath());
  }
  
  
  public void deleteFile(File file) {
    if(file.delete()) {
      slf4jLogger.info(file.getName() + " was deleted.");
    } else {
      slf4jLogger.debug("Could not delete file " + file.getName() + ".");
    }
  }
  
  public Object convertJsonToObject(String json, Object object) throws IOException {
    Object o = JSON.std.beanFrom(object.getClass(), json);
    return o;
  }
  
  
  
//    private void load() throws MissingConfigurationException {
//    try {
//      ClassLoader loader = Thread.currentThread().getContextClassLoader();
//      InputStream is = loader.getResourceAsStream("configuration/config.json");
//      try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
//        ObjectMapper mapper = new ObjectMapper();
//        config = mapper.readValue(br, Configuration.class);
//        br.close();
//      }
//    } catch (NullPointerException | IOException e) {
//      throw new MissingConfigurationException("The configuration file could not be located.");
//    }
//  }
  
  
//  private void scanDirectory() {
//
//  }
//
  
}
