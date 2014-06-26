/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.general;

import java.util.ArrayList;

/**
 *
 * @author BLI
 */
public class DataSet {
    
    private ArrayList<Float> dataSet;
    private float mean = 0;
    private float meanSquaredError = 0;
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
        meanSquaredError = currentSquaredError/numberDataPoints;
        meanError = currentError/numberDataPoints;
        
        
    }

    public float getMean() {
        return mean;
    }

    public float getMeanSquaredError() {
        return meanSquaredError;
    }

    public float getMeanError() {
        return meanError;
    }
    
}
