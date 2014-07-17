/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataInterpretation;

import java.nio.file.Path;
import org.zrd.util.dataWriting.DataWriterHelper;
import org.zrd.util.dataWriting.ProbeDataWriter;

/**
 *
 * @author BLI
 */
public class SerialDataRecorder {
    
    private ProbeDataWriter xyDataWriter;
    private ProbeDataWriter yawPitchRollDataWriter;
    private ProbeDataWriter outputDataWriter;

    public SerialDataRecorder(Path filePath){
        xyDataWriter = ProbeDataWriter.getNewWriter(filePath, "pathXYData");
        yawPitchRollDataWriter = ProbeDataWriter.getNewWriter(filePath, "yawPitchRollData");
        outputDataWriter = ProbeDataWriter.getNewWriter(filePath, "outputData");
    }
    
    public void addLineToFiles(float deltaX, float deltaY, float yaw, float pitch, float roll){
        ProbeDataWriter.writeLineInWriter(xyDataWriter, 
                DataWriterHelper.getPositionOutputText(deltaX, deltaY));
        ProbeDataWriter.writeLineInWriter(yawPitchRollDataWriter, 
                DataWriterHelper.getOrientationOutputText(yaw, pitch, roll));
        ProbeDataWriter.writeLineInWriter(outputDataWriter, 
                DataWriterHelper.getOutputText(deltaX, deltaY, yaw, pitch, roll));
    }
    
    public void closeRecording(){
        ProbeDataWriter.closeWriter(xyDataWriter);
        ProbeDataWriter.closeWriter(yawPitchRollDataWriter);
        ProbeDataWriter.closeWriter(outputDataWriter);
    }
    
    
}
