/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.qualityRecorder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.zrd.qualityTracker.QualityReader;
import org.zrd.serialDataInterpreter.dataReader.SerialDataReader;

/**
 *
 * @author BLI
 */
public class QualityReader_QualityRecorder {

    public static QualityReader createQualityReader(){
        Properties dataRecorderProperties = Properties_QualityRecorder.getProperties();
        Path filePath = Paths.get(dataRecorderProperties.getProperty("pathRecording.filePath"));
        SerialDataReader serialReader = new SerialDataReader(dataRecorderProperties);
        return new QualityReader(serialReader,filePath);
    }
    
    
}
