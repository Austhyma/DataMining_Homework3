/* 
 * File: KNearestNeighbor.java
 */

import java.util.*;
import java.io.*;

public class KNearestNeighbor {
  
  public static ArrayList<Data> processData(String fileName, boolean test) throws IOException{
    
    BufferedReader file;
    ArrayList<String> attributeNames = new ArrayList<String>();
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
    int iD = 0;
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
      if (!test) allData.add(new Data(objects, classLabel, iD));
      else allData.add(new TestingData(objects, classLabel, iD));
      iD++;
      line = file.readLine();
    }
    return allData;
  }
  
  public static ArrayList<TestingData> kNearestNeighbor(ArrayList<TestingData> testingData, ArrayList<Data> trainingData, int k) {
    for (int i = 0; i < testingData.size(); i++) {
      int[] smallestE = new int[k];
      int[] smallestM = new int[k];
      int[] smallestC = new int[k];
      int[] smallestCosine = new int[k];
      TestingData current = testingData.get(i);
      for (int j = 0; j < k; j++) {
        int smallE = 0;
        int smallM = 0;
        int smallC = 0;
        int smallCosine = 0;
        for (int l = 1; l < current.arraysize(); l++) {
          if (current.getEuclidean(l) < current.getEuclidean(smallE)) smallE = l;
          if (current.getManhattan(l) < current.getManhattan(smallM)) smallM = l;
          if (current.getChebyshev(l) < current.getChebyshev(smallC)) smallC = l;
          if (current.getCosine(l) < current.getCosine(smallCosine)) smallCosine = l;
        }
        smallestE[j] = smallE;
        smallestM[j] = smallM;
        smallestC[j] = smallC;
        smallestCosine[j] = smallCosine;
        current.getEuclideans().remove(smallE);
        current.getManhattans().remove(smallM);
        current.getChebyshevs().remove(smallC);
        current.getCosines().remove(smallCosine);
        current.setArraySize(current.arraysize()-1);
        
      }
      String smallEClass = "";
      for (int m = 0; m < k; m++) {
        for (int n = 0; n < trainingData.size(); n++) {
          if (trainingData.get(n).getID() == smallestE[m]) {
            if (m == 0) smallEClass = "
            smallEClass = trainingData.get(m).getClassLabel
    }
    return testingData;
  }
    
  
  public static void main(String[] args) throws IOException {
    
    //Reads/processes training/testing files
    ArrayList<Data> trainingData = processData(args[0], false);
    if (trainingData == null) {System.out.println("Invalid training file."); return;}
    ArrayList<Data> initialTestingData = processData(args[1], true);
    if (initialTestingData == null) {System.out.println("Invalid testing file."); return;}
    System.out.println("Finished Reading Files");
    
    //Compute distances
    ArrayList<TestingData> testingData = new ArrayList<TestingData>();
    for (Iterator<Data> testObj = initialTestingData.iterator(); testObj.hasNext();) {
      TestingData current = (TestingData) testObj.next();
      current.computeDistances(trainingData, trainingData.size());
      testingData.add(current);
    }
    System.out.println("Finished Computing Distances");
    
    ArrayList<TestingData> k3 = kNearestNeighbor(testingData, 3);
    ArrayList<TestingData> k5 = kNearestNeighbor(testingData, 5);
    ArrayList<TestingData> k7 = kNearestNeighbor(testingData, 7);
    ArrayList<TestingData> k9 = kNearestNeighbor(testingData, 9);
    ArrayList<TestingData> k11 = kNearestNeighbor(testingData, 11);
  }
}