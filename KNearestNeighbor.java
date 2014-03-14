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
      //System.out.println(i);
      TestingData current = testingData.get(i);
      HashMap<Integer, Double> smallestEuclideans = getSmallest(current.getEuclideans(), k);
      HashMap<Integer, Double> smallestManhattans = getSmallest(current.getManhattans(), k);
      HashMap<Integer, Double> smallestChebyshevs = getSmallest(current.getChebyshevs(), k);
      HashMap<Integer, Double> smallestCosines = getSmallest(current.getCosines(), k);
      current.setEuclideanPrediction(vote(smallestEuclideans, trainingData));
      current.setManhattanPrediction(vote(smallestManhattans, trainingData));
      current.setChebyshevPrediction(vote(smallestChebyshevs, trainingData));
      current.setCosinePrediction(vote(smallestCosines, trainingData));
    }
    return testingData;
  }
  
  public static HashMap<Integer, Double> getSmallest(HashMap<Integer, Double> currentVals, int k) {
    HashMap<Integer, Double> returnVals = new HashMap<Integer, Double>();
    for (int i = 0; i < k; i++) {
      //System.out.println(i);
      int small = 0;
      double smallValue = 0.0;
      for (int j = 1; j < currentVals.size(); j++) {
        //System.out.println(j);
        //System.out.println(!returnVals.containsKey(j));
        if (!returnVals.containsKey(j)) continue;
        if (currentVals.get(j) < currentVals.get(small)) {small = j; smallValue = currentVals.get(j);}
      }
      returnVals.put(small, smallValue);
      //System.out.println(returnVals.toString());
      //System.out.println(returnVals.containsKey(small));
      currentVals.remove(small);
    }
    return returnVals;
  }
    
    
  public static String vote(HashMap<Integer, Double> smallestValues, ArrayList<Data> tData) {
    String classLabel1 = "";
    String classLabel2 = "";
    double yesVotes = 0;
    double noVotes = 0;
    for (int m = 0; m < smallestValues.size(); m++) {
      for (int n = 0; n < tData.size(); n++) {
        if (tData.get(n).getID() == smallestValues.get(m)) {
          if (m == 0) {classLabel1 = tData.get(n).getClassLabel(); yesVotes += (1/Math.pow(smallestValues.get(m), 2));}
          else if (tData.get(n).getClassLabel().equals(classLabel1)) yesVotes += (1/Math.pow(smallestValues.get(m), 2));
          else {classLabel2 = tData.get(n).getClassLabel(); noVotes += (1/Math.pow(smallestValues.get(m), 2));}
        }
      }
    }
    return (yesVotes > noVotes) ? classLabel1 : classLabel2;
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
    
    ArrayList<TestingData> k3 = kNearestNeighbor(testingData, trainingData, 3);
    ArrayList<TestingData> k5 = kNearestNeighbor(testingData, trainingData, 5);
    ArrayList<TestingData> k7 = kNearestNeighbor(testingData, trainingData, 7);
    ArrayList<TestingData> k9 = kNearestNeighbor(testingData, trainingData, 9);
    ArrayList<TestingData> k11 = kNearestNeighbor(testingData, trainingData, 11);
    System.out.println("Predictions Made");
    
    String[] classLabels = new String[2];
    classLabels[0] = trainingData.get(0).getClassLabel();
    for (int i = 1; i < trainingData.size(); i++) {
      if (!trainingData.get(i).getClassLabel().equals(classLabels[0])) {classLabels[1] = trainingData.get(i).getClassLabel(); break;}
    }
    System.out.println(classLabels[0]);
    System.out.println(classLabels[1]);
  }
}