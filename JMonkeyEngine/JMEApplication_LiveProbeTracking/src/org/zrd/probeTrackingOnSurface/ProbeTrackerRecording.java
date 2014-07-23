/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTrackingOnSurface;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import java.io.File;
import org.zrd.geometryToolkit.pathDataStructure.RecordedPathSet;
import org.zrd.geometryToolkit.pathDataStructure.SegmentSet;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;
import org.zrd.probeTracking.ProbeTracker;

/**
 *
 * @author BLI
 */
public class ProbeTrackerRecording extends GeneralKeyboardActionMethod{
    
    private ProbeTracker probeTracker;
    private String recordingText;
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
        recordingText = probeTracker.getRecordingText();
        if(probeTracker.isNewPathExists()){
            recordedPathSet.addPath(probeTracker.getCurrentPathVertices());
            
            SegmentSet recordedPath = new SegmentSet(probeTracker.getCurrentPathVertices());
            System.out.println("Arc Length of Recorded Path: " + recordedPath.getArcLength());
            
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
