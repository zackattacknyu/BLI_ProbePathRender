/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.qualityTracker;

import org.zrd.util.dataStreaming.ProbeDataStream;
import org.zrd.util.dataStreaming.StreamQualityTracker;

/**
 *
 * @author BLI
 */
public class QualityReader implements ProbeDataStream{
    
    private StreamQualityTracker currentQualityTracker;
    
    public QualityReader(StreamQualityTracker currentQualityTracker){
        this.currentQualityTracker = currentQualityTracker;
    }

    @Override
    public void updateData() {
        currentQualityTracker.updateData();
        
        System.out.println("Current Quality: " + currentQualityTracker.getCurrentQuality());
    }

    @Override
    public void startStopRecording() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
}
