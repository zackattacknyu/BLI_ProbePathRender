/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataReader;

import java.nio.file.Path;
import org.zrd.util.dataWriting.ProbeDataWriter;

/**
 *
 * @author BLI
 */
public class RawSerialRecorder {
    
    private ProbeDataWriter rawOutputWriter;

    public RawSerialRecorder(Path filePath){
        rawOutputWriter = ProbeDataWriter.getNewWriter(filePath, "rawSerialOutput");
    }
    
    public void addLineToFiles(String rawOutputLine){
        ProbeDataWriter.writeLineInWriter(rawOutputWriter, rawOutputLine);
    }
    
    public void closeRecording(){
        ProbeDataWriter.closeWriter(rawOutputWriter);
    }
    
    
}
