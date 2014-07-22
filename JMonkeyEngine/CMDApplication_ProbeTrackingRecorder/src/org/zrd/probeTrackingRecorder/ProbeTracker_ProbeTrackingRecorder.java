/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTrackingRecorder;

import com.jme3.math.Vector3f;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.zrd.probeTracking.ProbeTracker;
import org.zrd.serialDataInterpreter.dataInterpretation.SerialDataInterpreter;
import org.zrd.util.trackingInterface.AbstractInputSourceTracker;

/**
 *
 * @author BLI
 */
public class ProbeTracker_ProbeTrackingRecorder {
    
    public static final Vector3f STARTING_POSITION = new Vector3f(0,0,0);
    
    public static final float INIT_SCALE_X = 0.00001f,INIT_SCALE_Y = 0.00001f;
    
    public static ProbeTracker createNewProbeTracker(){
        Properties trackerProps = Properties_ProbeTrackingRecorder.getProperties();
        AbstractInputSourceTracker currentSourceTracker;
        short displacementMode;
        
        currentSourceTracker = new SerialDataInterpreter(trackerProps);

        displacementMode = Short.parseShort(
                trackerProps.getProperty("trackDisplacementMode"));
        
        Path filePath = Paths.get(trackerProps.getProperty("pathRecording.filePath"));
        
        return ProbeTracker.initializeProbeTracker(
                currentSourceTracker, displacementMode, filePath, 
                INIT_SCALE_X, INIT_SCALE_Y, STARTING_POSITION);
    }
    
}
