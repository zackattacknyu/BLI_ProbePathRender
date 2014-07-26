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
public class QualityStatistics {
    
    
    private DataSet qualityStats;
    
    
    public QualityStatistics(){
        qualityStats = new DataSet(50);
    }
    
    public void addToStats(float quality){
        qualityStats.addToDataSet(quality);
    }
    
    public ArrayList<String> closeStatRecording(){
        qualityStats.processData();
        qualityStats.displayResults();
        
        return qualityStats.getResultStrings();
    }
}
