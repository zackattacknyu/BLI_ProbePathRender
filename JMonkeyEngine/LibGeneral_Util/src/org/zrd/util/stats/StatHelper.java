/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.stats;

/**
 *
 * @author BLI
 */
public class StatHelper {
    
    public static double getMean(Double[] values){
        double totalVal = 0;
        for(Double val: values){
            totalVal += val;
        }
        double numVals = values.length;
        return totalVal/numVals;
    }
    
    public static IndexEntry getMax(Double[] values){
        int index = 0;
        double value = Double.MIN_VALUE;
        for(int i = 0; i < values.length; i++){
            if(values[i] > value){
                value = values[i];
                index = i;
            }
        }
        return new IndexEntry(index,value);
    }
    
}
