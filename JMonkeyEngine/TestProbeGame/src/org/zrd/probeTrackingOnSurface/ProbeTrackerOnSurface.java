/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTrackingOnSurface;

import com.jme3.math.Vector3f;
import org.zrd.geometryToolkit.geometryUtil.ProgramConstants;
import org.zrd.geometryToolkit.meshTraversal.PathProjectionOntoMesh;
import org.zrd.probeTracking.ProbeTracker;

/**
 *
 * @author BLI
 */
public class ProbeTrackerOnSurface {
    
    
    private ProbeTracker probeTracker;
    private ProbeRotationCalibration probeRotCalib;
    private Vector3f lastPosition;
    private PathProjectionOntoMesh pathProj;
    
    public ProbeTrackerOnSurface(ProbeTracker probeTracker, ProbeRotationCalibration probeRotCalib){
        this.probeRotCalib = probeRotCalib;
        this.probeTracker = probeTracker;
        lastPosition = probeTracker.getCurrentPosition();
    }
    
    public void updateData(){
        probeTracker.updateData();
        
        if(probeRotCalib.doesNewRotCalibExists()){
            
            pathProj = new PathProjectionOntoMesh(probeRotCalib.getCurrentTriangle(),
                    probeTracker.getCurrentPosition(),probeRotCalib.getMeshInfo());
            lastPosition = probeTracker.getCurrentPosition().clone();
            
        }else if(probeRotCalib.isRotationCalibrationDone()){
            
            Vector3f currentProbePosition = probeTracker.getCurrentPosition().clone();
            float distance = currentProbePosition.distance(lastPosition);
            if(distance > ProgramConstants.MIN_SEGMENT_LENGTH){
                Vector3f segmentVec = currentProbePosition.subtract(lastPosition);
                pathProj.getCurrentProjectedPath(segmentVec);
                lastPosition = pathProj.getCurrentStartPoint().clone();
                probeTracker.setCurrentPosition(pathProj.getCurrentStartPoint());
                System.out.println("Last Position: " + currentProbePosition);
                System.out.println("Position Now: " + lastPosition);
                System.out.println("Current Triangle: " + pathProj.getCurrentTriangle());
            }
            
        }
        
        
    }
    
}