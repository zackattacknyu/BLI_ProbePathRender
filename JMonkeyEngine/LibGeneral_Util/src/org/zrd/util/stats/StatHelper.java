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
    
}
