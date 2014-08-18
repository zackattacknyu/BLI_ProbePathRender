/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTrackingRender;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import org.zrd.geometryToolkit.pathDataStructure.RecordedPathSet;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;
import org.zrd.probeTracking.ProbeTracker;

/**
 *
 * @author BLI
 */
public class ProbeTrackerRecording extends GeneralKeyboardActionMethod{
    
    private ProbeTracker probeTracker;
    private RecordedPathSet recordedPathSet;
    private boolean newPathExists = false;
    
    public ProbeTrackerRecording(InputManager inputManager, RecordedPathSet recordedPathSet, ProbeTracker probeTracker){
        super(inputManager,"startStopNewPath",KeyInput.KEY_N);
        this.probeTracker = probeTracker;
        this.recordedPathSet = recordedPathSet;
    }

    @Override
    public void actionMethod() {
        probeTracker.startStopRecording();
        if(probeTracker.isNewPathExists()){
            recordedPathSet.addPath(probeTracker.getCurrentPathVertices());
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
