/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTrackingOnSurface;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometryUtil.MiscGeometryHelper;
import org.zrd.geometryToolkit.geometryUtil.ProgramConstants;
import org.zrd.geometryToolkit.locationTracking.LocationTracker;
import org.zrd.geometryToolkit.locationTracking.RotationCalibrationTool;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.meshTraversal.PathProjectionOntoMesh;

/**
 *
 * @author BLI
 */
public class ProbeTrackerOnSurface implements LocationTracker{
    
    
    private LocationTracker locationTracker;
    private RotationCalibrationTool rotCalibrationTool;
    private Vector3f currentPositionOnMesh;
    private PathProjectionOntoMesh pathProj;
    private TriangleSet surfaceToTrackOn;
    
    private MeshTriangle currentTriangle;
    private MeshTriangle lastTriangle;

    public ProbeTrackerOnSurface(LocationTracker probeTracker, RotationCalibrationTool probeRotCalib, TriangleSet surfaceToTrackOn){
        this.rotCalibrationTool = probeRotCalib;
        this.locationTracker = probeTracker;
        this.surfaceToTrackOn = surfaceToTrackOn;
        currentPositionOnMesh = probeTracker.getCurrentPosition();
    }

    public Vector3f getCurrentPosition() {
        return currentPositionOnMesh;
    }
    
    public void updateData(){
        locationTracker.updateData();
        
        if(rotCalibrationTool.isCalibrationNewlyFinished()){
            
            pathProj = new PathProjectionOntoMesh(rotCalibrationTool.getTriangleAtEndPoint(),
                    rotCalibrationTool.getCalibEndPoint().clone(),surfaceToTrackOn);
            currentPositionOnMesh = rotCalibrationTool.getCalibEndPoint().clone();
            locationTracker.resetDisplacementSinceLastPoint();
            lastTriangle = rotCalibrationTool.getTriangleAtEndPoint();
            
            System.out.println("Triangle at end point: " + lastTriangle);
            
        }else if(rotCalibrationTool.isCalibrationDone()){
            
            Vector3f currentSegment = locationTracker.getDisplacementSinceLastPoint();
            if(currentSegment.length() > ProgramConstants.MIN_SEGMENT_LENGTH){
                
                ArrayList<Vector3f> segmentsOnMesh = pathProj.getCurrentProjectedPath(currentSegment);
                currentPositionOnMesh = segmentsOnMesh.get(segmentsOnMesh.size()-1);
                locationTracker.resetDisplacementSinceLastPoint();
                
                currentTriangle = pathProj.getCurrentTriangle();
                if(currentTriangle == null){
                    System.out.println("CurrentTriangle is null");
                }
                else if(!currentTriangle.equals(lastTriangle)){
                    System.out.println("Current Triangle: " + currentTriangle);
                    lastTriangle = currentTriangle;
                }
                
            }
            
            
        }else{
            
            currentPositionOnMesh = locationTracker.getCurrentPosition();
            
        }
        
        
    }

    public Vector3f getCurrentDisplacement() {
        return locationTracker.getCurrentDisplacement();
    }

    public Vector3f getDisplacementSinceLastPoint() {
        return locationTracker.getDisplacementSinceLastPoint();
    }

    public void resetDisplacementSinceLastPoint() {
        locationTracker.resetDisplacementSinceLastPoint();
    }

    public String getXYZtext() {
        return MiscGeometryHelper.getXYZDisplayString(currentPositionOnMesh);
    }

    public String getYawPitchRollText() {
        return locationTracker.getYawPitchRollText();
    }

    public boolean isRecordingPath() {
        return locationTracker.isRecordingPath();
    }

    public Quaternion getLocalRotation() {
        return locationTracker.getLocalRotation();
    }

    public ArrayList<Vector3f> getVerticesSinceLastRead() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public float getArcLengthSinceLastRead() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public float getTrackingQuality() {
        return locationTracker.getTrackingQuality();
    }
    
}
