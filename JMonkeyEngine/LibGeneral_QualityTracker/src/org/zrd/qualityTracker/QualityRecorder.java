/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.qualityTracker;

import java.nio.file.Path;
import org.zrd.util.dataWriting.ProbeDataWriter;

/**
 *
 * @author BLI
 */
public class QualityRecorder {

    private ProbeDataWriter qualityWriter;

    /**
     * Initializes a quality recording to text file
     * @param filePath      path to put the text file
     */
    public QualityRecorder(Path filePath){
        qualityWriter = ProbeDataWriter.getNewWriter(filePath, "qualityValues");
    }
    
    /**
     * Adds a line to the current text file being generated
     * @param rawOutputLine         line to add to text file
     */
    public void addQualityLine(float currentQuality){
        ProbeDataWriter.writeLineInWriter(qualityWriter, String.valueOf(currentQuality));
    }
    
    /**
     * Closes the current recording
     */
    public void closeRecording(){
        ProbeDataWriter.closeWriter(qualityWriter);
    }    
    
}
