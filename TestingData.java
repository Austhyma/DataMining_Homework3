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
    Set<String> attNames = this.attributes.keySet();
    for (Iterator<String> att = attNames.iterator(); att.hasNext();) {
      String currentAtt = att.next();
      double manhattan = this.attributes.get(currentAtt) - trainingData.get(currentAtt);
      mtotal += manhattan;
      etotal += Math.pow(manhattan, 2);
    }
    euclideans.put(index, Math.sqrt(etotal));
    manhattans.put(index, mtotal);
  }
}