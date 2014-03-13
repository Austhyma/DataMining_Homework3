/*
 * File: Data.java
 */

import java.util.*;

public class Data {
  
  protected HashMap<String, Double> attributes;
  protected String classLabel;
  
  public Data(HashMap<String, Double> dimensions, String classLabel) {
    this.attributes = dimensions;
    this.classLabel = classLabel;
  }
  
  public String getClassLabel() {return classLabel;}
  public HashMap<String, Double> getAttributes() {return this.attributes;}
}