/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.bliProbePath;

import com.jme3.input.InputManager;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.zrd.keyboardObjectTracking.keyboardTrackingClient.KeyboardInputSourceTracker;
import org.zrd.probeTracking.ProbeTrackerClient;
import org.zrd.serialDataInterpreter.client.SerialInputSourceTracker;
import org.zrd.util.trackingInterface.AbstractInputSourceTracker;

/**
 *
 * @author BLI
 */
public class ProbeTracker_BLIProbePath {
    
    
    public static ProbeTrackerClient createNewProbeTrackerClient(InputManager manager){
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
        
        ProbeTrackerClient returnClient = new ProbeTrackerClient(
                currentSourceTracker,displacementMode,pathRecordingFilePath);

        return returnClient;
    }
    
}
