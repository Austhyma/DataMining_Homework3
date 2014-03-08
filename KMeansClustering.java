/* 
 * File: KMeansClustering.java
 */

import java.util.*;
import java.io.*;

public class KMeansClustering {
  
  public static ArrayList<Data> processData(String fileName) throws IOException{
    
    BufferedReader file;
    ArrayList<String> attributeNames = new ArrayList<String>();
    String[] firstClass;
    //Validate file
    try {
      file = new BufferedReader(new FileReader(fileName));
    }
    catch (FileNotFoundException e) {
      return null;
    }
    
    String line = file.readLine();
    while (!line.equals("@data")) {
      if (line.equals("")) {line = file.readLine();continue;}
      String[] lineArgs = line.split("\\s");
      if (lineArgs[0].equals("@relation")) {line = file.readLine();continue;}
      else if (lineArgs[0].equals("@attribute")) {
        attributeNames.add(lineArgs[1]);
        line = file.readLine();
      }
    }
    
    ArrayList<Data> allData = new ArrayList<Data>();
    line = file.readLine();
    while (line != null) {
      HashMap<String, Double> objects = new HashMap<String, Double>();
      String[] dataValues = line.split(",");
      String classLabel = "";
      for (int i = 0; i < dataValues.length; i++) {
        String attribute = attributeNames.get(i);
        if (attribute.equals("Class")) {
          classLabel = dataValues[i];
          line = file.readLine();
          continue;
        }
        double value;
        try {
          value = Double.parseDouble(dataValues[i]);
        }
        catch (NumberFormatException e) {
          line = file.readLine();
          continue;
        }
        objects.put(attribute, value);
      }
      allData.add(new Data(objects, classLabel));
      line = file.readLine();
    }
    return allData;
  }
  
  public static void main(String[] args) throws IOException {
    ArrayList<Data> trainingData = processData(args[0]);
    if (trainingData == null) {System.out.println("Invalid training file."); return;}
    ArrayList<Data> testingData = processData(args[1]);
    if (testingData == null) {System.out.println("Invalid testing file."); return;}
    
    System.out.println(trainingData.get(0).getClassLabel());
  }
}