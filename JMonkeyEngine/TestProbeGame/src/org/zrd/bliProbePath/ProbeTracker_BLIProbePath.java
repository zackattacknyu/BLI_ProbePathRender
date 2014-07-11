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
import org.zrd.probeTracking.ProbeTrackerClient;
import org.zrd.serialDataInterpreter.client.SerialInputSourceTracker;
import org.zrd.util.trackingInterface.AbstractInputSourceTracker;

/**
 *
 * @author BLI
 */
public class ProbeTracker_BLIProbePath {
    
    public static final Vector3f STARTING_POSITION = new Vector3f(-0.4f,-0.97f,-15.35f);
    
    public static final float INIT_SCALE_X = 0.00001f,INIT_SCALE_Y = 0.00001f;
    
    public static ProbeTracker createNewProbeTracker(InputManager manager){
        Properties trackerProps = Properties_BLIProbePath.getProperties();
        AbstractInputSourceTracker currentSourceTracker;
        short displacementMode;
        boolean debugTracking;
        
        debugTracking = Boolean.parseBoolean(trackerProps.getProperty("debugTracking"));
        
        if(debugTracking){
            currentSourceTracker = new KeyboardInputSourceTracker(manager);
        }else{
            currentSourceTracker = new SerialInputSourceTracker(trackerProps);
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
