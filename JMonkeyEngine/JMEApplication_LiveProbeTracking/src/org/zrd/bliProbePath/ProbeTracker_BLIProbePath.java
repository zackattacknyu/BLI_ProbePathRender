/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.bliProbePath;

import com.jme3.input.InputManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.zrd.keyboardObjectTracking.keyboardTrackingClient.KeyboardInputSourceTracker;
import org.zrd.probeTracking.ProbeTracker;
import org.zrd.serialDataInterpreter.dataInterpretation.SerialDataInterpreter;
import org.zrd.util.trackingInterface.AbstractInputSourceTracker;

/**
 *
 * @author BLI
 */
public class ProbeTracker_BLIProbePath {
    
    public static final Vector3f STARTING_POSITION = new Vector3f(-0.4f,-0.97f,-15.35f);
    
    /*
     * These were obtained through rigorous testing.
     * TODO: Detail the testing
     */
    public static final float INIT_SCALE_X = 0.000021118f,INIT_SCALE_Y = 0.000021118f;
    //public static final float INIT_SCALE_X = 0.000010726f,INIT_SCALE_Y = 0.000010726f;
    
    //private Quaternion rotationCalibration = new Quaternion();
    public static final Quaternion INIT_ROT_CALIB = new Quaternion(0.2559f,-0.3667f,0.5326f,0.7153f);

    public static ProbeTracker createNewProbeTracker(InputManager manager){
        Properties trackerProps = Properties_BLIProbePath.getProperties();
        
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
        
        Path pathRecordingFilePath = Paths.get("textFiles").
                resolve("logs").resolve("paths");
        
        //gets the scale factors
        float realToProbeFactor = Float.parseFloat(
                trackerProps.getProperty("scaleFactor.realToProbe"));
        float virtualToRealFactor = Float.parseFloat(
                trackerProps.getProperty("scaleFactor.virtualToReal"));
        float finalScaleFactor = realToProbeFactor*virtualToRealFactor;
        float finalScaleX = finalScaleFactor;
        float finalScaleY = finalScaleFactor;
        
        //gets the starting position
        float startX = Float.parseFloat(
                trackerProps.getProperty("initLocation.x"));
        float startY = Float.parseFloat(
                trackerProps.getProperty("initLocation.y"));
        float startZ = Float.parseFloat(
                trackerProps.getProperty("initLocation.z"));
        Vector3f startingPosition = new Vector3f(startX,startY,startZ);
        
        //gets the starting quaternion
        float quatW = Float.parseFloat(trackerProps.getProperty("initQuat.w"));
        float quatX = Float.parseFloat(trackerProps.getProperty("initQuat.x"));
        float quatY = Float.parseFloat(trackerProps.getProperty("initQuat.y"));
        float quatZ = Float.parseFloat(trackerProps.getProperty("initQuat.z"));
        Quaternion initQuat = new Quaternion(quatW,quatX,quatY,quatZ);
        
        return ProbeTracker.initializeProbeTracker(
                currentSourceTracker, displacementMode, pathRecordingFilePath, 
                finalScaleX, finalScaleY, startingPosition,initQuat);
    }
    
}
