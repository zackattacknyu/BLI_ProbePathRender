/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.qualityRecorder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.zrd.serialDataInterpreter.dataReader.SerialDataReader;

/**
 *
 * @author BLI
 */
public class SerialDataInterpreter_QualityRecorder {

    public static SerialDataReader createSerialInterpreter(){
        Properties dataRecorderProperties = Properties_QualityRecorder.getProperties();
        Path filePath = Paths.get(dataRecorderProperties.getProperty("pathRecording.filePath"));
        return new SerialDataReader(dataRecorderProperties,filePath);
    }
    
    
}
