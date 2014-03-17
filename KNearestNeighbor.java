/* 
 * File: KNearestNeighbor.java
 */

import java.util.*;
import java.io.*;
import java.math.*;

public class KNearestNeighbor {
  
  //Processes command line arguments, processes the data into useable data structures, and validates files
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
    file.close();
    return allData;
  }
  
  //Algorithm for kNearestNeighbor
  public static ArrayList<TestingData> kNearestNeighbor(ArrayList<TestingData> testingData, ArrayList<Data> trainingData, int k) {
    for (int i = 0; i < testingData.size(); i++) {
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
  
  //Finds the smallest k distances
  public static HashMap<Integer, Double> getSmallest(HashMap<Integer, Double> currentVals, int k) {
    HashMap<Integer, Double> returnVals = new HashMap<Integer, Double>();
    for (int i = 0; i < k; i++) {
      int smallIndex;
      if (!returnVals.containsKey(0)) smallIndex = 0;
      else smallIndex = 1;
      double smallValue = currentVals.get(smallIndex);
      for (int j = 1; j < currentVals.size(); j++) {
        if (returnVals.containsKey(j)) continue;
        if (currentVals.get(j) < currentVals.get(smallIndex)) {
          smallIndex = j; 
          smallValue = currentVals.get(j);
        }
      }
      returnVals.put(smallIndex, smallValue);
    }
    return returnVals;
  }
    
  //Uses the smallest values and the training data to figure out the result of the "vote"
  public static String vote(HashMap<Integer, Double> smallestValues, ArrayList<Data> tData) {
    String classLabel1 = tData.get(0).getClassLabel();
    String classLabel2 = "";
    BigDecimal yesVotes = new BigDecimal(0);
    BigDecimal noVotes = new BigDecimal(0);
    Set<Integer> keys = smallestValues.keySet();
    for (Iterator<Integer> key = keys.iterator(); key.hasNext();) {
      int currentKey = key.next();
      for (int n = 0; n < tData.size(); n++) {
        if (tData.get(n).getID() == currentKey) {
          Double val = 1/Math.pow(smallestValues.get(currentKey), 2);
          BigDecimal weightedVote;
          if ((val == 0) || val == Double.POSITIVE_INFINITY) weightedVote = new BigDecimal(0);
          else weightedVote = new BigDecimal(1/Math.pow(smallestValues.get(currentKey), 2));
          if (tData.get(n).getClassLabel().equals(classLabel1)) {
            yesVotes = yesVotes.add(weightedVote);
          }
          else {
            if (classLabel2.equals("")) classLabel2 = tData.get(n).getClassLabel();
            noVotes = noVotes.add(weightedVote);
          }
        }
      }
    }
    return (yesVotes.compareTo(noVotes) == 1) ? classLabel1 : classLabel2;
  }
  
  public static HashMap<String, HashMap<String, Goodness>> getGoodness(ArrayList<TestingData> testingData, String[] classLabel) {
    HashMap<String, HashMap<String, Goodness>> returnVals = new HashMap<String, HashMap<String, Goodness>>();
    for (int i = 0; i < classLabel.length; i++) {
      HashMap<String, Goodness> results = new HashMap<String, Goodness>();
      results.put("Euclidean", getConfusionMatrix(testingData, classLabel[i], 0));
      results.put("Manhattan", getConfusionMatrix(testingData, classLabel[i], 1));
      results.put("Chebyshev", getConfusionMatrix(testingData, classLabel[i], 2));
      results.put("Cosine", getConfusionMatrix(testingData, classLabel[i], 3));
      returnVals.put(classLabel[i], results);
    }
    return returnVals;
  }
  
  public static Goodness getConfusionMatrix(ArrayList<TestingData> testingData, String classLabel, int index) {
    double truePositive = 0;
    double falseNegative = 0;
    double trueNegative = 0;
    double falsePositive = 0;
    for (int i = 0; i < testingData.size(); i++) {
      TestingData current = testingData.get(i);
      switch (index) {
        case 0: if (current.getPredictedEuclidean().equals(classLabel) && current.getPredictedEuclidean().equals(classLabel)) truePositive+=1;
        else if (current.getPredictedEuclidean().equals(classLabel) && !current.getPredictedEuclidean().equals(classLabel)) falsePositive+=1;
        else if (!current.getPredictedEuclidean().equals(classLabel) && !current.getPredictedEuclidean().equals(classLabel)) trueNegative+=1;
        else if (!current.getPredictedEuclidean().equals(classLabel) && current.getPredictedEuclidean().equals(classLabel)) falseNegative+=1;
        break;
        case 1: if (current.getPredictedManhattan().equals(classLabel) && current.getPredictedManhattan().equals(classLabel)) truePositive+=1;
        else if (current.getPredictedManhattan().equals(classLabel) && !current.getPredictedManhattan().equals(classLabel)) falsePositive+=1;
        else if (!current.getPredictedManhattan().equals(classLabel) && !current.getPredictedManhattan().equals(classLabel)) trueNegative+=1;
        else if (!current.getPredictedManhattan().equals(classLabel) && current.getPredictedManhattan().equals(classLabel)) falseNegative+=1;
        break;
        case 2: if (current.getPredictedChebyshev().equals(classLabel) && current.getPredictedChebyshev().equals(classLabel)) truePositive+=1;
        else if (current.getPredictedChebyshev().equals(classLabel) && !current.getPredictedChebyshev().equals(classLabel)) falsePositive+=1;
        else if (!current.getPredictedChebyshev().equals(classLabel) && !current.getPredictedChebyshev().equals(classLabel)) trueNegative+=1;
        else if (!current.getPredictedChebyshev().equals(classLabel) && current.getPredictedChebyshev().equals(classLabel)) falseNegative+=1;
        break;
        case 3: if (current.getPredictedCosine().equals(classLabel) && current.getPredictedCosine().equals(classLabel)) truePositive+=1;
        else if (current.getPredictedCosine().equals(classLabel) && !current.getPredictedCosine().equals(classLabel)) falsePositive+=1;
        else if (!current.getPredictedCosine().equals(classLabel) && !current.getPredictedCosine().equals(classLabel)) trueNegative+=1;
        else if (!current.getPredictedCosine().equals(classLabel) && current.getPredictedCosine().equals(classLabel)) falseNegative+=1;
        break;
      }
    }
    //System.out.println(truePositive);
    return new Goodness(truePositive, falsePositive, trueNegative, falseNegative);
  }
  
  public static void output(HashMap<String, HashMap<String, Goodness>> k3Values, HashMap<String, HashMap<String, Goodness>> k5Values, HashMap<String, HashMap<String, Goodness>> k7Values, HashMap<String, HashMap<String, Goodness>> k9Values, HashMap<String, HashMap<String, Goodness>> k11Values, String[] classLabels) throws IOException{
    String filename = "";
    for (int i = 0; i < classLabels.length; i++) {
      filename += classLabels[i] + "_";
    }
    PrintWriter outFile = new PrintWriter(new FileWriter(filename + "results.csv"));
    
    for (int i = 0; i < classLabels.length; i++) {
      String tableColumnLabels = "Euclidean, Chebyshev, Manhattan, Cosine Similarity";
      String[] tableColumnLabel = {"Euclidean", "Chebyshev", "Manhattan", "Cosine"};
      String[] goodnessMeasures = {"Precision", "Recall", "F1 Measures"};
      for (int j = 0; j < goodnessMeasures.length; j++) {
        outFile.println(classLabels[i] + " " + goodnessMeasures[j] + ", " + tableColumnLabels);
        String line3 = "k=3, " + addToLine(k3Values, classLabels[i], goodnessMeasures[j], tableColumnLabel);
        outFile.println(line3);
        String line5 = "k=5, " + addToLine(k5Values, classLabels[i], goodnessMeasures[j], tableColumnLabel);
        outFile.println(line5);
        String line7 = "k=7, " + addToLine(k7Values, classLabels[i], goodnessMeasures[j], tableColumnLabel);
        outFile.println(line7);
        String line9 = "k=9, " + addToLine(k9Values, classLabels[i], goodnessMeasures[j], tableColumnLabel);
        outFile.println(line9);
        String line11 = "k=11, " + addToLine(k11Values, classLabels[i], goodnessMeasures[j], tableColumnLabel);
        outFile.println(line11);
      }
    }
    outFile.close();
  }
  
  public static String addToLine(HashMap<String, HashMap<String, Goodness>> values, String classLabel, String goodnessMeasure, String[] columnLabels) {
    String line = "";
    if (goodnessMeasure.equals("Precision")) {
      for (int i = 0; i < columnLabels.length; i++) {
        Goodness current = values.get(classLabel).get(columnLabels[i]);
        line += current.getPrecision() + ", ";
      }
    }
    else if (goodnessMeasure.equals("Recall")) {
      for (int i = 0; i < columnLabels.length; i++) {
        Goodness current = values.get(classLabel).get(columnLabels[i]);
        line += current.getRecall() + ", ";
      }
    }
    if (goodnessMeasure.equals("F1 Measures")) {
      for (int i = 0; i < columnLabels.length; i++) {
        Goodness current = values.get(classLabel).get(columnLabels[i]);
        line += current.getPrecision() + ", ";
      }
    }
    return line;
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
    HashMap<String, HashMap<String, Goodness>> k3Values = getGoodness(k3, classLabels);
    HashMap<String, HashMap<String, Goodness>> k5Values = getGoodness(k5, classLabels);
    HashMap<String, HashMap<String, Goodness>> k7Values = getGoodness(k7, classLabels);
    HashMap<String, HashMap<String, Goodness>> k9Values = getGoodness(k9, classLabels);
    HashMap<String, HashMap<String, Goodness>> k11Values = getGoodness(k11, classLabels);
    System.out.println("Goodness Measures Computed");
    
    //System.out.println(k3Values.get(classLabels[0]).get("Euclidean").getPrecision());
    output(k3Values, k5Values, k7Values, k9Values, k11Values, classLabels);
    System.out.println("Finished");
  }
}