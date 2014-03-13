/* 
 * File: KNearestNeighbor.java
 */

import java.util.*;
import java.io.*;

public class KNearestNeighbor {
  
  public static ArrayList<Data> processData(String fileName, boolean test) throws IOException{
    
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
      if (!test) allData.add(new Data(objects, classLabel));
      else allData.add(new TestingData(objects, classLabel));
      line = file.readLine();
    }
    return allData;
  }
  
  public static void main(String[] args) throws IOException {
    
    //Reads/processes training/testing files
    ArrayList<Data> trainingData = processData(args[0], false);
    if (trainingData == null) {System.out.println("Invalid training file."); return;}
    ArrayList<Data> initialTestingData = processData(args[1], true);
    if (initialTestingData == null) {System.out.println("Invalid testing file."); return;}
    
    ArrayList<TestingData> testingData = new ArrayList<TestingData>();
    for (Iterator<Data> testObj = initialTestingData.iterator(); testObj.hasNext();) {
      TestingData current = (TestingData) testObj.next();
      current.computeDistances(trainingData, trainingData.size());
      testingData.add(current);
    }
  }
}