/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.meshTraversal;

import com.jme3.math.Quaternion;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import org.zrd.util.dataHelp.OutputHelper;
import org.zrd.util.dataWriting.ProbeDataWriter;
import org.zrd.util.fileHelper.FileDataHelper;

/**
 *
 * @author BLI
 */
public class CalibrationHelper {
    
    public static void writeCalibrationResults(float uniformScaleFactor, 
            Quaternion rotationCalibration,
            ArrayList<String> qualityStats, Path resultFolder) {
        
        ArrayList<String> resultText = new ArrayList<String>(10);
        resultText.add("Uniform Scale Factor: ");
        resultText.add(String.valueOf(uniformScaleFactor));
        resultText.add(OutputHelper.EMPTY_LINE_STRING);
        resultText.add("Rotation Calibration Quat: ");
        resultText.add(String.valueOf(rotationCalibration));
        resultText.add(OutputHelper.EMPTY_LINE_STRING);
        resultText.add("Rotation Calibration Matrix: ");
        resultText.add(String.valueOf(rotationCalibration.toRotationMatrix().getRow(0)));
        resultText.add(String.valueOf(rotationCalibration.toRotationMatrix().getRow(1)));
        resultText.add(String.valueOf(rotationCalibration.toRotationMatrix().getRow(2)));
        resultText.add(OutputHelper.EMPTY_LINE_STRING);
        resultText.add("Quality Statistics: ");
        resultText.addAll(qualityStats);
                
        try {
            FileDataHelper.exportLinesToFile(resultText, 
                    ProbeDataWriter.getNewDataFilePath(
                    resultFolder, "CalibrationResults"));
        } catch (IOException ex) {
            System.out.println("Error Writing Calibration Results: " + ex);
        }
    }
    
}
