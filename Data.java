/*
 * File: Data.java
 */

import java.util.*;

public class Data {
  
  private HashMap<String, Double> dimensions;
  private String classLabel;
  
  public Data(HashMap<String, Double> dimensions, String classLabel) {
    this.dimensions = dimensions;
    this.classLabel = classLabel;
  }
  
  public String getClassLabel() {return classLabel;}
  public HashMap<String, Double> getDimensions() {return this.dimensions;}
}