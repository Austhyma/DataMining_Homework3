/*
 * File: TrainingData.java
 */
import java.util.*;

public class TestingData extends Data {
  
  private String predictedClassLabel;
  private HashMap<Integer, Double> euclideans;
  private HashMap<Integer, Double> chebyshevs;
  private HashMap<Integer, Double> manhattans;
  private HashMap<Integer, Double> cosines;
  
  public TestingData(HashMap<String, Double> attributes, String classLabel) {
    super(attributes, classLabel);
  }
  
  public void setPrediction(String classLabel) {this.predictedClassLabel = classLabel;}
  public String getPrediction() {return predictedClassLabel;}
  
  public void computeDistances(ArrayList<Data> trainingData, int size) {
    this.euclideans = new HashMap<Integer, Double>(size);
    this.chebyshevs = new HashMap<Integer, Double>(size);
    this.manhattans = new HashMap<Integer, Double>(size);
    this.cosines = new HashMap<Integer, Double>(size);
    
    int i = 0;
    for (Iterator<Data> curTrain = trainingData.iterator(); curTrain.hasNext();) {
      setEuclideansManhattans(curTrain.next().getAttributes(), i);
      i++;
    }
  }
  
  public void setEuclideansManhattans(HashMap<String, Double> trainingData, int index) {
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
  
  public HashMap<Integer, Double> getEuclideans() {return this.euclideans;}
  public HashMap<Integer, Double> getManhattans() {return this.manhattans;}
  public HashMap<Integer, Double> getChebyshevs() {return this.chebyshevs;}
  public HashMap<Integer, Double> getCosines() {return this.cosines;}
  
  public static void main(String[] args) {
    String[] attNames = {"x", "y", "z"};
    ArrayList<Data> trainingData = new ArrayList<Data>();
    
    HashMap<String, Double> attribute = new HashMap<String, Double>();
    attribute.put("x", 8.0);
    attribute.put("y", 4.0);
    attribute.put("z", 4.0);
    Data newData = new Data(attribute, "blah");
    trainingData.add(newData);
    attribute = new HashMap<String, Double>();
    attribute.put("x", 2.0);
    attribute.put("y", 1.0);
    attribute.put("z", 6.0);
    Data newData1 = new Data(attribute, "blah");
    trainingData.add(newData1);
    attribute = new HashMap<String, Double>();
    attribute.put("x", 1.0);
    attribute.put("y", 2.0);
    attribute.put("z", 3.0);
    Data newData2 = new Data(attribute, "blah");
    trainingData.add(newData1);
    
    attribute = new HashMap<String, Double>();
    attribute.put("x", 1.0);
    attribute.put("y", 1.0);
    attribute.put("z", 2.0);
    TestingData test = new TestingData(attribute, "blah");
    
    test.computeDistances(trainingData, trainingData.size());
    System.out.println("Manhattans = " + test.getManhattans().toString());
    System.out.println("Euclideans = " + test.getEuclideans().toString());
    System.out.println("Chebyshevs = " + test.getChebyshevs().toString());
    System.out.println("Cosines = " + test.getCosines().toString());
  }
}