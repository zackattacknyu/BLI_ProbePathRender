/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialdatarecorder;

import org.zrd.serialDataInterpreter.dataInterpretation.SerialDataInterpreter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.zrd.util.dataStreaming.DataStreamRecorder;
/**
 *
 * @author BLI
 */
public class SerialDataRecorder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //doRotationTesting();
        
        Properties dataRecorderProperties = Properties_SerialDataRecorder.getProperties();
        Path filePath = Paths.get(dataRecorderProperties.getProperty("pathRecording.filePath"));
        final SerialDataInterpreter serialData = new SerialDataInterpreter(dataRecorderProperties,filePath);
        
        DataStreamRecorder.startStreamingService(serialData);
    }
    
    
}
