/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.rawSerialRecorder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.zrd.serialDataInterpreter.dataReader.SerialDataReader;

/**
 *
 * @author BLI
 */
public class SerialDataReader_RawSerialRecorder {

    public static SerialDataReader createSerialReader(){
        Properties dataRecorderProperties = Properties_RawSerialRecorder.getProperties();
        Path filePath = Paths.get(dataRecorderProperties.getProperty("pathRecording.filePath"));
        return new SerialDataReader(dataRecorderProperties,filePath);
    }
    
    
}
