/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.agent.file;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author henke
 */
public class FileLoaderTest {
  

  
  public FileLoaderTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  @Test
  public void testSomeMethod() throws IOException {
    FileLoader fl = new FileLoader();
    String directory = "configuration";
    List<String> list = fl.listFilesInDirectory(directory);
    String json = fl.loadJsonFromFile(directory, list.get(0));
    System.out.println(json);
    
    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }
  
}
