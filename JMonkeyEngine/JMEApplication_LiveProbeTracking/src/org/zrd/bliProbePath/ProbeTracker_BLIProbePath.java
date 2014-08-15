/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.bliProbePath;

import com.jme3.input.InputManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.util.Properties;
import org.zrd.geometryToolkit.geometryUtil.CalibrationFileResults;
import org.zrd.keyboardObjectTracking.keyboardTrackingClient.KeyboardInputSourceTracker;
import org.zrd.probeTracking.ProbeTracker;
import org.zrd.serialDataInterpreter.dataInterpretation.SerialDataInterpreter;
import org.zrd.util.fileHelper.FilePathHelper;
import org.zrd.util.properties.PropertiesHelper;
import org.zrd.util.trackingInterface.AbstractInputSourceTracker;

/**
 *
 * @author BLI
 */
public class ProbeTracker_BLIProbePath {

    public static ProbeTracker createNewProbeTracker(InputManager manager){
        Properties trackerProps = PropertiesHelper.getDefaultProperties();
        
        AbstractInputSourceTracker currentSourceTracker;
        short displacementMode;
        boolean debugTracking;
        
        debugTracking = Boolean.parseBoolean(trackerProps.getProperty("debugTracking"));
        
        if(debugTracking){
            currentSourceTracker = new KeyboardInputSourceTracker(manager);
        }else{
            currentSourceTracker = new SerialDataInterpreter(trackerProps);
        }

        displacementMode = Short.parseShort(
                trackerProps.getProperty("trackDisplacementMode"));
        
        CalibrationFileResults results = new CalibrationFileResults(trackerProps);

        float finalScaleX = results.getScaleCalib();
        float finalScaleY = results.getScaleCalib();
        
        //gets the starting position
        float startX = Float.parseFloat(
                trackerProps.getProperty("initLocation.x"));
        float startY = Float.parseFloat(
                trackerProps.getProperty("initLocation.y"));
        float startZ = Float.parseFloat(
                trackerProps.getProperty("initLocation.z"));
        Vector3f startingPosition = new Vector3f(startX,startY,startZ);
        
        Quaternion initQuat = results.getRotationCalib();
        
        return ProbeTracker.initializeProbeTracker(
                currentSourceTracker, displacementMode, 
                FilePathHelper.getDefaultOutputFolder(), 
                finalScaleX, finalScaleY, startingPosition,initQuat);
    }
    
}
