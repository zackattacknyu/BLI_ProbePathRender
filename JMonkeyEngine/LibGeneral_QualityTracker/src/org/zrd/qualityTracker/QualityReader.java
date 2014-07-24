/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.qualityTracker;

import java.nio.file.Path;
import org.zrd.util.dataStreaming.ProbeDataStream;
import org.zrd.util.dataStreaming.StreamQualityTracker;

/**
 *
 * @author BLI
 */
public class QualityReader implements ProbeDataStream{
    
    private StreamQualityTracker currentQualityTracker;
    private QualityRecorder currentQualityRecorder;
    private boolean recordingQuality = false;
    private Path recordingFilePath;
    
    
    public QualityReader(StreamQualityTracker currentQualityTracker, Path recordingFilePath){
        this.currentQualityTracker = currentQualityTracker;
        this.recordingFilePath = recordingFilePath;
    }

    @Override
    public void updateData() {
        currentQualityTracker.updateData();
        
        float qual = currentQualityTracker.getCurrentQuality();
        if(recordingQuality){
            currentQualityRecorder.addQualityLine(qual);
        }
        
        //output quality to console
        System.out.println(qual);
    }

    @Override
    public void startStopRecording() {
        if(recordingQuality){
            currentQualityRecorder.closeRecording();
            recordingQuality = false;
        }else{
            currentQualityRecorder = new QualityRecorder(recordingFilePath);
            recordingQuality = true;
        }
    }
    
}
