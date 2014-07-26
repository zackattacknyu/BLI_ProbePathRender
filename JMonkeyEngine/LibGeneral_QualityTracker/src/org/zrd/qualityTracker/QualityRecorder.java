/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.qualityTracker;

import java.io.IOException;
import java.nio.file.Path;
import org.zrd.util.dataWriting.ProbeDataWriter;
import org.zrd.util.fileHelper.FileDataHelper;
import org.zrd.util.stats.DataSet;

/**
 *
 * @author BLI
 */
public class QualityRecorder {

    private ProbeDataWriter qualityWriter;
    private QualityStatistics currentQualitySet;
    private Path recordingFilePath;

    /**
     * Initializes a quality recording to text file
     * @param filePath      path to put the text file
     */
    public QualityRecorder(Path filePath){
        recordingFilePath = filePath;
        qualityWriter = ProbeDataWriter.getNewWriter(filePath, "qualityValues");
        currentQualitySet = new QualityStatistics();
    }
    
    /**
     * Adds a line to the current text file being generated
     * @param rawOutputLine         line to add to text file
     */
    public void addQualityLine(float currentQuality){
        ProbeDataWriter.writeLineInWriter(qualityWriter, String.valueOf(currentQuality));
        currentQualitySet.addToStats(currentQuality);
    }
    
    /**
     * Closes the current recording
     */
    public void closeRecording(){
        ProbeDataWriter.closeWriter(qualityWriter);
        
        try {
            FileDataHelper.exportLinesToFile(currentQualitySet.closeStatRecording(), 
                    ProbeDataWriter.getNewDataFilePath(recordingFilePath, "qualityStats"));
        } catch (IOException ex) {
            System.out.println("Error trying to write stats to file: " + ex);
        }
    }    
    
}
