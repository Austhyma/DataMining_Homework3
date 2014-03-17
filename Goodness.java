public class Goodness {
  
  private double truePositive;
  private double falsePositive;
  private double trueNegative;
  private double falseNegative;
  
  private double recall;
  private double precision;
  private double f1;
  
  public Goodness(double truePositive, double falsePositive, double trueNegative, double falseNegative) {
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
  
  public static void main(String[] args) {
    Goodness g1 = new Goodness(2, 5, 5, 3);
    System.out.println(g1.getRecall());
    System.out.println(g1.getPrecision());
    System.out.println(g1.getF1());
  }
}