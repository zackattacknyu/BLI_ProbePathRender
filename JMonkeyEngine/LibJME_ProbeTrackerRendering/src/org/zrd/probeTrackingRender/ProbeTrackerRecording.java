/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTrackingRender;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import org.zrd.geometryToolkit.locationTracking.LocationTracker;
import org.zrd.geometryToolkit.pathDataStructure.RecordedPathSet;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;

/**
 *
 * @author BLI
 */
public class ProbeTrackerRecording extends GeneralKeyboardActionMethod{
    
    private LocationTracker activeTracker;
    private RecordedPathSet recordedPathSet;
    private boolean newPathExists = false;
    
    public ProbeTrackerRecording(InputManager inputManager, RecordedPathSet recordedPathSet, LocationTracker activeTracker){
        super(inputManager,"startStopNewPath",KeyInput.KEY_N);
        this.activeTracker = activeTracker;
        this.recordedPathSet = recordedPathSet;
    }

    @Override
    public void actionMethod() {
        activeTracker.startStopRecording();
        if(activeTracker.isNewPathExists()){
            recordedPathSet.addPath(activeTracker.getCurrentPathVertices());
            newPathExists = true;
        }
    }
    
    public boolean isNewPathExists() {
        if(newPathExists){
            newPathExists = false;
            return true;
        }else{
            return false;
        }
    }
    
}
