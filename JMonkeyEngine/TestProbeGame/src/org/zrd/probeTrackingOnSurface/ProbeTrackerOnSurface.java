/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTrackingOnSurface;

import org.zrd.probeTracking.ProbeTracker;

/**
 *
 * @author BLI
 */
public class ProbeTrackerOnSurface {
    
    
    private ProbeTracker probeTracker;
    private ProbeRotationCalibration probeRotCalib;
    
    public ProbeTrackerOnSurface(ProbeTracker probeTracker, ProbeRotationCalibration probeRotCalib){
        this.probeRotCalib = probeRotCalib;
        this.probeTracker = probeTracker;
    }
    
}
