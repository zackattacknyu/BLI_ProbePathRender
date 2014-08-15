/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.stats;

import java.util.ArrayList;

/**
 *
 * @author BLI
 */
public class DataSet {
    
    private ArrayList<Float> dataSet;
    private ArrayList<String> resultStrings;
    private float mean = 0;
    private float min = Float.MAX_VALUE;
    private float max = Float.MIN_VALUE;
    private float variance = 0;
    private float standardDeviation = 0;
    private float meanError = 0;

    public DataSet(int estNumElements) {
        dataSet = new ArrayList<Float>(estNumElements);
    }
    public DataSet(){
        this(10);
    }
    
    public void addToDataSet(Float number){
        dataSet.add(number);
        
        if(number < min) min = number;
        
        if(number > max) max = number;
    }
    
    public void processData(){
        float numberDataPoints = dataSet.size();
        float currentSum = 0;
        float currentDifference;
        float currentError = 0;
        float currentSquaredError = 0;
        
        for(Float number: dataSet){
            currentSum = currentSum + number;
        }
        mean = currentSum/numberDataPoints;
        
        for(Float number: dataSet){
            currentDifference = Math.abs(number-mean);
            currentError = currentError + currentDifference;
            currentSquaredError = currentSquaredError + currentDifference*currentDifference;
        }
        variance = currentSquaredError/numberDataPoints;
        standardDeviation = (float) Math.sqrt(variance);
        meanError = currentError/numberDataPoints;
        
        resultStrings = new ArrayList<String>(9);
        resultStrings.add(" ");
        resultStrings.add("Quality Statistics for Path: ");
        resultStrings.add("Mean: " + mean);
        resultStrings.add("Mean Error: " + meanError);
        resultStrings.add("Variance: " + variance);
        resultStrings.add("Standard Deviation: " + standardDeviation);
        resultStrings.add("Min: " + min);
        resultStrings.add("Max: " + max);
        resultStrings.add(" ");
        
    }
    
    public void displayResults(){
        for(String result: resultStrings){
            System.out.println(result);
        }
    }
    
    public ArrayList<String> getResultStrings(){
        return resultStrings;
    }

    public float getMean() {
        return mean;
    }

    public float getVariance() {
        return variance;
    }

    public float getStandardDeviation() {
        return standardDeviation;
    }
    
    public float getMeanError() {
        return meanError;
    }
    
}
