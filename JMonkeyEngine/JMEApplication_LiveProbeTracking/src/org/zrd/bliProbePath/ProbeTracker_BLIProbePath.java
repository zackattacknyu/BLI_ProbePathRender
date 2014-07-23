/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.bliProbePath;

import com.jme3.input.InputManager;
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
    public static final float INIT_SCALE_X = 0.0002321f,INIT_SCALE_Y = 0.0002321f;
    
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
        
        return ProbeTracker.initializeProbeTracker(
                currentSourceTracker, displacementMode, pathRecordingFilePath, 
                INIT_SCALE_X, INIT_SCALE_Y, STARTING_POSITION);
    }
    
}
