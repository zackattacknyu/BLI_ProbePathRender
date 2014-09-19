/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.nio.file.Path;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometryUtil.GeometryOutputHelper;
import org.zrd.geometryToolkit.locationTracking.LocationTracker;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.meshTraversal.PathProjectionOntoMesh;
import org.zrd.geometryToolkit.pathTools.PathHelper;
import org.zrd.util.dataHelp.DataArrayToStringConversion;

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
    private boolean recordingPath = false;
    private PathRecorder currentRecordingPathOnMesh;
    private Path outputFilePath;

    public ProbeTrackerOnSurface(LocationTracker probeTracker, TriangleSet surfaceToTrackOn, Path outputFilePath){
        this.locationTracker = probeTracker;
        this.surfaceToTrackOn = surfaceToTrackOn;
        currentPositionOnMesh = probeTracker.getCurrentPosition();
        this.outputFilePath = outputFilePath;
        pathProj = null;
    }
    
    @Override
    public void setCurrentTriangle(MeshTriangle triangle) {
        pathProj = new PathProjectionOntoMesh(triangle,locationTracker.getCurrentPosition(),surfaceToTrackOn);
        locationTracker.resetDisplacementSinceLastPoint();
        lastTriangle = triangle;
    }

    @Override
    public Vector3f getCurrentPosition() {
        return currentPositionOnMesh;
    }
    
    @Override
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

            }else{
                currentPositionOnMesh = locationTracker.getCurrentPosition();
            }
        }
        
        if(recordingPath){
            currentRecordingPathOnMesh.addToPath(currentPositionOnMesh);
        }
        
        
    }

    @Override
    public Vector3f getCurrentDisplacement() {
        return locationTracker.getCurrentDisplacement();
    }

    @Override
    public Vector3f getDisplacementSinceLastPoint() {
        return locationTracker.getDisplacementSinceLastPoint();
    }

    @Override
    public void resetDisplacementSinceLastPoint() {
        locationTracker.resetDisplacementSinceLastPoint();
    }

    @Override
    public String getXYZtext() {
        return GeometryOutputHelper.getXYZDisplayString(currentPositionOnMesh);
    }

    @Override
    public String getYawPitchRollText() {
        return locationTracker.getYawPitchRollText();
    }

    @Override
    public boolean isRecordingPath() {
        return locationTracker.isRecordingPath();
    }

    @Override
    public Quaternion getLocalRotation() {
        return locationTracker.getLocalRotation();
    }

    @Override
    public ArrayList<Vector3f> getVerticesSinceLastRead() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float getArcLengthSinceLastRead() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float getTrackingQuality() {
        return locationTracker.getTrackingQuality();
    }

    @Override
    public void setCurrentPosition(Vector3f position) {
        locationTracker.setCurrentPosition(position);
    }

    @Override
    public void startStopRecording() {
        //starts and stops the tracking for the probe tracker
        locationTracker.startStopRecording();
        
        if(recordingPath){
            System.out.println("Recording New Path Stopped");
            currentRecordingPathOnMesh.closeRecording();
            recordingPath = false;
        }else{
            System.out.println("Now Recording new path");
            currentRecordingPathOnMesh = new PathRecorder(currentPositionOnMesh,outputFilePath,true);
            recordingPath = true;
        }
        
    }

    @Override
    public boolean isNewPathExists() {
        return locationTracker.isNewPathExists();
    }

    @Override
    public ArrayList<Vector3f> getCurrentPathVertices() {
        return currentRecordingPathOnMesh.getVertices();
    }

    @Override
    public Vector3f getTrackerNormal() {
        return locationTracker.getTrackerNormal();
    }
    
    @Override
    public Vector3f getTrackerX(){
        return locationTracker.getTrackerX();
    }
    
    @Override
    public Vector3f getTrackerY(){
        return locationTracker.getTrackerY();
    }

    @Override
    public String[] getCurrentDataStrings() {
        return locationTracker.getCurrentDataStrings();
    }

    @Override
    public void setDataArrayToStringConvertor(DataArrayToStringConversion converter) {
        locationTracker.setDataArrayToStringConvertor(converter);
    }

    
    
}
