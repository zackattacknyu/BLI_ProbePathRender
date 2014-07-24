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
    private float mean = 0;
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
        
        
    }
    
    public void displayResults(){
        System.out.println("Mean: " + mean);
        System.out.println("Mean Error: " + meanError);
        System.out.println("Variance: " + variance);
        System.out.println("Standard Deviation: " + standardDeviation);
        
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
