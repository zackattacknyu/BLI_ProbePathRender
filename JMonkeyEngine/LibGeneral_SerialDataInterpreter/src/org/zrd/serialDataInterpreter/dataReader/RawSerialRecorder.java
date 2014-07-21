/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataReader;

import java.nio.file.Path;
import org.zrd.util.dataWriting.ProbeDataWriter;

/**
 * This records raw serial data. It takes in the file path
 *      and initializes the writer that puts the raw
 *      data into a text file
 *
 * @author BLI
 */
public class RawSerialRecorder {
    
    private ProbeDataWriter rawOutputWriter;

    /**
     * Initializes a raw serial data recording to text file
     * @param filePath      path to put the text file
     */
    public RawSerialRecorder(Path filePath){
        rawOutputWriter = ProbeDataWriter.getNewWriter(filePath, "rawSerialOutput");
    }
    
    /**
     * Adds a line to the current text file being generated
     * @param rawOutputLine         line to add to text file
     */
    public void addLineToFiles(String rawOutputLine){
        ProbeDataWriter.writeLineInWriter(rawOutputWriter, rawOutputLine);
    }
    
    /**
     * Closes the current recording
     */
    public void closeRecording(){
        ProbeDataWriter.closeWriter(rawOutputWriter);
    }
    
    
}
