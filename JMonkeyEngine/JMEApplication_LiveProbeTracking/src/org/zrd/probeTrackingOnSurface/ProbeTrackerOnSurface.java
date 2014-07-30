/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTrackingOnSurface;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometryUtil.GeometryOutputHelper;
import org.zrd.geometryToolkit.locationTracking.LocationTracker;
import org.zrd.geometryToolkit.probeCalibration.RotationCalibrationTool;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.meshTraversal.PathProjectionOntoMesh;
import org.zrd.geometryToolkit.pathTools.PathHelper;

/**
 *
 * @author BLI
 */
public class ProbeTrackerOnSurface implements LocationTracker{
    
    
    private LocationTracker locationTracker;
    private Vector3f currentPositionOnMesh;
    private PathProjectionOntoMesh pathProj;
    private TriangleSet surfaceToTrackOn;
    
    private MeshTriangle currentTriangle;
    private MeshTriangle lastTriangle;

    public ProbeTrackerOnSurface(LocationTracker probeTracker, TriangleSet surfaceToTrackOn){
        this.locationTracker = probeTracker;
        this.surfaceToTrackOn = surfaceToTrackOn;
        currentPositionOnMesh = probeTracker.getCurrentPosition();
        pathProj = null;
    }
    
    public void setCurrentTriangle(MeshTriangle triangle) {
        pathProj = new PathProjectionOntoMesh(triangle,locationTracker.getCurrentPosition(),surfaceToTrackOn);
        locationTracker.resetDisplacementSinceLastPoint();
        lastTriangle = triangle;
    }

    public Vector3f getCurrentPosition() {
        return currentPositionOnMesh;
    }
    
    public void updateData(){
        locationTracker.updateData();
        
        if(pathProj == null){
            
            currentPositionOnMesh = locationTracker.getCurrentPosition();
            
        }else{
            
            Vector3f currentSegment = locationTracker.getDisplacementSinceLastPoint();
            if(currentSegment.length() > PathHelper.MIN_SEGMENT_LENGTH){

                ArrayList<Vector3f> segmentsOnMesh = pathProj.getCurrentProjectedPath(currentSegment);
                currentPositionOnMesh = PathHelper.getLastPoint(segmentsOnMesh);
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
        return GeometryOutputHelper.getXYZDisplayString(currentPositionOnMesh);
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

    public void setCurrentPosition(Vector3f position) {
        locationTracker.setCurrentPosition(position);
    }

    
    
}
