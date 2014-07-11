/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialdatarecorder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.zrd.serialDataInterpreter.dataInterpretation.SerialDataInterpreter;

/**
 *
 * @author BLI
 */
public class SerialInterpreter_SerialDataRecorder {

    public static SerialDataInterpreter createSerialInterpreter(){
        Properties dataRecorderProperties = Properties_SerialDataRecorder.getProperties();
        Path filePath = Paths.get(dataRecorderProperties.getProperty("pathRecording.filePath"));
        return new SerialDataInterpreter(dataRecorderProperties,filePath);
    }
    
    
}
