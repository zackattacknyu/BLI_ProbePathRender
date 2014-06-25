/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;

/**
 *
 * @author BLI
 */
public class LowPassFilterData {
    
    
    private ArrayList<Float> data;
    private int window;
    private float average;
    
    public LowPassFilterData(int windowSize){
        data = new ArrayList<Float>(windowSize);
        window = windowSize;
    }
    
    public float getAverage(){
        return average;
    }
    
    private void recalculateAverage(){
        float sum = 0;
        for(Float number:data){
            sum = sum + number;
        }
        average = sum/window;
    }
    
    public void addToData(float number){
        data.add(number);
        if(data.size()>window){
            data.remove(0);
            recalculateAverage();
        }
        
    }
    
}
