/* 
 * File: KMeansClustering.java
 */

import java.util.*;
import java.io.*;

public class KMeansClustering {
  
  public static ArrayList<Data> processData(String fileName) throws IOException{
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
    while (line != null) {
      String[] lineArgs = line.split(" ");
      if (!lineArgs[0].equals("@attribute")) {
        line = file.readLine();
        continue;
      }
      while (lineArgs.length != 2) {
        
    }
    
  }
  
  public static void main(String[] args) {
    ArrayList<Data> trainingData = processData(args[0]);
    if (trainingData == null) {System.out.println("Invalid training file."); return;}
    ArrayList<Data> testingData = processData(args[1]);
    if (testingData == null) {System.out.println("Invalid testing file."); return;}
  }
}