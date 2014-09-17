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
    
    public static double getMean(double[] values){
        double totalVal = 0;
        for(Double val: values){
            totalVal += val;
        }
        return totalVal/values.length;
    }
    
    public static IndexEntry getMax(Double[] values, int start, int end){
        int index = 0;
        double value = Double.MIN_VALUE;
        for(int i = start; i < end; i++){
            if(values[i] > value){
                value = values[i];
                index = i;
            }
        }
        return new IndexEntry(index,value);
    }
    
    public static IndexEntry getMax(Double[] values){
        return getMax(values,0,values.length);
    }
    
}
