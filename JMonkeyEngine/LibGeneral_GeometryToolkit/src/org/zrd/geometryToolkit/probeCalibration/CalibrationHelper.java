/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.probeCalibration;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.nio.file.Path;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometricCalculations.AngleAxisRotation;
import org.zrd.geometryToolkit.geometricCalculations.RotationTransformHelper;
import org.zrd.geometryToolkit.geometryUtil.CalibrationProperties;
import org.zrd.geometryToolkit.geometryUtil.GeometryOutputHelper;
import org.zrd.util.dataHelp.BasicAngleHelper;
import org.zrd.util.dataHelp.OutputHelper;
import org.zrd.util.fileHelper.FileDataHelper;
import org.zrd.util.fileHelper.GeneralFileHelper;

/**
 *
 * @author BLI
 */
public class CalibrationHelper {
    
    public static void writeCalibrationResults(
            float uniformScaleFactor, 
            Quaternion previousRotCalibration,
            Quaternion newRotation,
            ArrayList<String> qualityStats, 
            ArrayList<String> reflectionCalibResults, 
            Path resultFolder) {
        
        AngleAxisRotation rotCalib = new AngleAxisRotation(newRotation);
        Quaternion currentRotCalib = newRotation.clone().mult(previousRotCalibration.clone());
        
        ArrayList<String> resultText = new ArrayList<String>(10);
        resultText.add("Uniform Scale Factor: ");
        resultText.add(String.valueOf(uniformScaleFactor));
        resultText.add(OutputHelper.EMPTY_LINE_STRING);
        
        resultText.add("New Rotation Quat: ");
        resultText.add(String.valueOf(newRotation));
        resultText.add(OutputHelper.EMPTY_LINE_STRING);
        
        resultText.add("New Rotation Axis: " + rotCalib.getAxis());
        resultText.add("New Rotation Angle: " + BasicAngleHelper.convertRadiansToDegrees(rotCalib.getAngle()));
        resultText.add(OutputHelper.EMPTY_LINE_STRING);
        
        resultText.add("Current Rotation Quat: ");
        resultText.add(String.valueOf(currentRotCalib));
        resultText.add(OutputHelper.EMPTY_LINE_STRING);
        
        resultText.add("#Add this next block to properties file if this is the desired rotation calibration");
        resultText.addAll(CalibrationProperties.getCalibrationPropertiesStrings(currentRotCalib));
        resultText.add(OutputHelper.EMPTY_LINE_STRING);
        
        resultText.add("Here are the reflection calibration results:");
        resultText.addAll(reflectionCalibResults);
        resultText.add(OutputHelper.EMPTY_LINE_STRING);
        
        resultText.addAll(qualityStats);
                
        exportCalibrationInfo(resultText,resultFolder,"CalibrationResults");
        
    }
    
    public static void writeAlignNormalResults(Vector3f probeNormal,Vector3f triangleNormal,Path resultFolder){
        Quaternion rotationOfNormal = RotationTransformHelper.getRotationFromVectors(probeNormal, triangleNormal);

        ArrayList<String> resultText = new ArrayList<>(10);
        resultText.add("Probe Normal: " + probeNormal);
        resultText.add("Triangle Normal: " + triangleNormal);
        resultText.add("Rotation to apply: " + rotationOfNormal);
        resultText.add(OutputHelper.EMPTY_LINE_STRING);
        resultText.add("#Add this next block to properties file to align normals at chosen point");
        resultText.addAll(CalibrationProperties.getCalibrationPropertiesStrings(rotationOfNormal));
        
        exportCalibrationInfo(resultText,resultFolder,"AlignNormalResults");
    }
    
    public static void exportCalibrationInfo(ArrayList<String> resultText,Path resultFolder, String prefix){
        FileDataHelper.exportLinesToFile(
                resultText,GeneralFileHelper.getNewDataFilePath(resultFolder, prefix));
    }
    
}
