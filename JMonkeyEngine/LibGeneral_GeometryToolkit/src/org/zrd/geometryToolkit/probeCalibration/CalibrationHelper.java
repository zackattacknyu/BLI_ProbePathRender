/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.probeCalibration;

import com.jme3.math.Quaternion;
import java.nio.file.Path;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometryUtil.CalibrationFileResults;
import org.zrd.util.dataHelp.OutputHelper;
import org.zrd.util.fileHelper.FileDataHelper;
import org.zrd.util.fileHelper.GeneralFileHelper;

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
        resultText.add("#Add this next block to properties file if this is the desired rotation calibration");
        resultText.addAll(CalibrationFileResults.getCalibrationPropertiesStrings(rotationCalibration));
        resultText.add(OutputHelper.EMPTY_LINE_STRING);
        resultText.add("Rotation Calibration Matrix: ");
        resultText.add(String.valueOf(rotationCalibration.toRotationMatrix().getRow(0)));
        resultText.add(String.valueOf(rotationCalibration.toRotationMatrix().getRow(1)));
        resultText.add(String.valueOf(rotationCalibration.toRotationMatrix().getRow(2)));
        resultText.addAll(qualityStats);
                
        FileDataHelper.exportLinesToFile(resultText,GeneralFileHelper.getNewDataFilePath(resultFolder, "CalibrationResults"));
    }
    
}
