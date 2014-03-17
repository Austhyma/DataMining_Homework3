/*
 * File: TrainingData.java
 */
import java.util.*;

public class TestingData extends Data {
  
  //Fields
  private String predictedEuclideanClass;
  private String predictedManhattanClass;
  private String predictedChebyshevClass;
  private String predictedCosineClass;
  private HashMap<Integer, Double> euclideans;
  private HashMap<Integer, Double> chebyshevs;
  private HashMap<Integer, Double> manhattans;
  private HashMap<Integer, Double> cosines;
  private int arraysize;
  
  //Constructor
  public TestingData(HashMap<String, Double> attributes, String classLabel, int iD) {
    super(attributes, classLabel, iD);
  }
  
  //Getters and setters
  public void setEuclideanPrediction(String classLabel) {this.predictedEuclideanClass = classLabel;}
  public void setManhattanPrediction(String classLabel) {this.predictedManhattanClass = classLabel;}
  public void setChebyshevPrediction(String classLabel) {this.predictedChebyshevClass = classLabel;}
  public void setCosinePrediction(String classLabel) {this.predictedCosineClass = classLabel;}
  public String getPredictedEuclidean() {return predictedEuclideanClass;}
  public String getPredictedManhattan() {return predictedManhattanClass;}
  public String getPredictedChebyshev() {return predictedChebyshevClass;}
  public String getPredictedCosine() {return predictedCosineClass;}
  public int arraysize() {return arraysize;}
  public double getEuclidean(int index) {return this.euclideans.get(index);}
  public double getManhattan(int index) {return this.manhattans.get(index);}
  public double getChebyshev(int index) {return this.chebyshevs.get(index);}
  public double getCosine(int index) {return this.cosines.get(index);}
  public void setArraySize(int size) {this.arraysize = size;}
  public HashMap<Integer, Double> getEuclideans() {return this.euclideans;}
  public HashMap<Integer, Double> getManhattans() {return this.manhattans;}
  public HashMap<Integer, Double> getChebyshevs() {return this.chebyshevs;}
  public HashMap<Integer, Double> getCosines() {return this.cosines;}
  
  
  public void computeDistances(ArrayList<Data> trainingData, int size) {
    this.euclideans = new HashMap<Integer, Double>(size);
    this.chebyshevs = new HashMap<Integer, Double>(size);
    this.manhattans = new HashMap<Integer, Double>(size);
    this.cosines = new HashMap<Integer, Double>(size);
    this.arraysize = size;
    
    for (Iterator<Data> curTrain = trainingData.iterator(); curTrain.hasNext();) {
      Data current = curTrain.next();
      //Refactored calculations for clarification
      computeAll(current.getAttributes(), current.getID());
    }
  }
  
  public void computeAll(HashMap<String, Double> trainingData, int index) {
    double etotal = 0;
    double mtotal = 0;
    double chebyshev = 0; //Can't think of a better way at this moment
    double cosineNumerator = 0;
    double cosineDenominator1 = 0;
    double cosineDenominator2 = 0;
    Set<String> attNames = this.attributes.keySet();
    for (Iterator<String> att = attNames.iterator(); att.hasNext();) {
      String currentAtt = att.next();
      double manhattan = Math.abs(this.attributes.get(currentAtt) - trainingData.get(currentAtt));
      mtotal += manhattan;
      etotal += Math.pow(manhattan, 2);
      if (manhattan > chebyshev) chebyshev = manhattan;
      cosineNumerator += this.attributes.get(currentAtt) * trainingData.get(currentAtt);
      cosineDenominator1 += Math.pow(this.attributes.get(currentAtt), 2);
      cosineDenominator2 += Math.pow(trainingData.get(currentAtt), 2);
    }
    euclideans.put(index, Math.sqrt(etotal));
    manhattans.put(index, mtotal);
    chebyshevs.put(index, chebyshev);
    cosines.put(index, cosineNumerator/(Math.sqrt(cosineDenominator1) * Math.sqrt(cosineDenominator2)));
  }
  
  //A main function for testing purposes
  public static void main(String[] args) {
    String[] attNames = {"x", "y", "z"};
    ArrayList<Data> trainingData = new ArrayList<Data>();
    
    HashMap<String, Double> attribute = new HashMap<String, Double>();
    attribute.put("x", 8.0);
    attribute.put("y", 4.0);
    attribute.put("z", 4.0);
    Data newData = new Data(attribute, "blah", 0);
    trainingData.add(newData);
    attribute = new HashMap<String, Double>();
    attribute.put("x", 2.0);
    attribute.put("y", 1.0);
    attribute.put("z", 6.0);
    Data newData1 = new Data(attribute, "blah", 0);
    trainingData.add(newData1);
    attribute = new HashMap<String, Double>();
    attribute.put("x", 1.0);
    attribute.put("y", 2.0);
    attribute.put("z", 3.0);
    Data newData2 = new Data(attribute, "blah", 0);
    trainingData.add(newData1);
    
    attribute = new HashMap<String, Double>();
    attribute.put("x", 1.0);
    attribute.put("y", 1.0);
    attribute.put("z", 2.0);
    TestingData test = new TestingData(attribute, "blah", 0);
    
    test.computeDistances(trainingData, trainingData.size());
    System.out.println("Manhattans = " + test.getManhattans().toString());
    System.out.println("Euclideans = " + test.getEuclideans().toString());
    System.out.println("Chebyshevs = " + test.getChebyshevs().toString());
    System.out.println("Cosines = " + test.getCosines().toString());
  }
}