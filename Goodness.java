public class Goodness {
  
  private int truePositive;
  private int falsePositive;
  private int trueNegative;
  private int falseNegative;
  
  private double recall;
  private double precision;
  private double f1;
  
  public Goodness(int truePositive, int falsePositive, int trueNegative, int falseNegative) {
    this.truePositive = truePositive;
    this.falsePositive = falsePositive;
    this.trueNegative = trueNegative;
    this.falseNegative = falseNegative;
    computeGoodnessValues();
  }
  
  public void computeGoodnessValues() {
    if ((truePositive + falsePositive) == 0) this.recall = 0;
    else this.recall = truePositive/(truePositive + falsePositive);
    if ((truePositive + falseNegative) == 0) this.precision = 0;
    else this.precision = truePositive/(truePositive + falseNegative);
    if ((precision + recall) == 0) this.f1 = 0;
    else this.f1 = (2*precision*recall)/(precision + recall);
  }
  
  public double getRecall() {return this.recall;}
  public double getPrecision() {return this.precision;}
  public double getF1() {return this.f1;}
}