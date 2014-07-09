/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking;

import org.zrd.probeTracking.deviceToWorldConversion.TrackingHelper;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import org.zrd.probeTracking.deviceToWorldConversion.AbstractSerialInputToWorldConverter;
import org.zrd.util.trackingInterface.AbstractInputSourceTracker;

/**
 *
 * This is meant to track the probe. 
 * 
 * It calls the data interpreter and tracks the probe in 3D space
 *      for Main.java
 * 
 * @author BLI
 */
public class ProbeTracker {

    private final Vector3f startingPosition;
    
    private float currentYaw=0,currentPitch = 0,currentRoll = 0;
    
    private Quaternion localRotation;
    private Vector3f currentPosition;
    private Vector2f currentXYPosition;
    
    private boolean recordingPath = false;
    
    private PathRecorder currentRecordingPath;
    
    private boolean newPathExists = false;
    private float currentDeltaX=0,currentDeltaY=0;
    private AbstractInputSourceTracker currentSourceTracker;
    private AbstractSerialInputToWorldConverter coordConverter;
    
    public ProbeTracker(AbstractSerialInputToWorldConverter coordConvertor, 
            AbstractInputSourceTracker currentSourceTracker,
            Vector3f startingPosition){
        this.coordConverter = coordConvertor;
        this.currentSourceTracker = currentSourceTracker;
        
        this.startingPosition = startingPosition;
        
        currentPosition = startingPosition.clone();
        
        currentXYPosition = new Vector2f(startingPosition.getX(),startingPosition.getY());
        
    }
    
    public void updateValues(){
        
        currentSourceTracker.updateData();
        
        if(currentSourceTracker.canBeginTracking()){
            currentYaw = currentSourceTracker.getCurrentYawRadians();
            currentPitch = currentSourceTracker.getCurrentPitchRadians();
            currentRoll = currentSourceTracker.getCurrentRollRadians();
            
            currentDeltaX = currentSourceTracker.getDeltaX();
            currentDeltaY = currentSourceTracker.getDeltaY();
        }
        
        localRotation = TrackingHelper.getQuaternion(currentYaw,currentPitch,currentRoll);
        
        //gets the current displacement vector
        Vector3f currentDisp = coordConverter.getXYZDisplacement(
                currentDeltaX, currentDeltaY, 
                currentYaw, currentPitch, currentRoll);
        
        //adds the displacement to current position
        currentPosition.addLocal(currentDisp);
        
        //gets the xy displacement vector
        Vector2f currentXYDisp = coordConverter.getXYDisplacement(
                currentDeltaX, currentDeltaY);
        
        currentXYPosition.addLocal(currentXYDisp);

        //here we record the xyz path
        if(recordingPath){
            currentRecordingPath.addToPath(currentPosition,currentXYPosition,
                    currentYaw, currentPitch, currentRoll);
        }
        
    }
    
    public Vector3f getCurrentNormal(){
        return coordConverter.getCurrentNormal();
    }
    
    public void updatePathRecording(){
        
        if(recordingPath){
            currentRecordingPath.closeRecording();
            newPathExists = true;
            recordingPath = false;
        }else{
            newPathExists = false;
            currentRecordingPath = new PathRecorder(currentPosition);
            recordingPath = true;
        }
        
        
    }

    public void resetProbe(){
        
        setCurrentPosition(startingPosition);
        setRotation(0,0,0);
        
    }

    public boolean isRecordingPath() {
        return recordingPath;
    }
    
    public Vector3f getCurrentPosition(){
        return currentPosition;
    }
    
    public void setCurrentPosition(Vector3f position){
        currentPosition = position;
        
    }
    
    public void setRotation(float yaw, float pitch, float roll){
        currentYaw = yaw;
        currentPitch = pitch;
        currentRoll = roll;
    }

    public float getCurrentPitch() {
        return currentPitch;
    }

    public float getCurrentRoll() {
        return currentRoll;
    }

    public void setFilterMode(int filterMode){
        currentSourceTracker.setFilterMode(filterMode);
    }
    
    public void startStopCalibration(){
        currentSourceTracker.startStopCalibration();
    }
    
    public boolean isCalibrating(){
        return currentSourceTracker.isCalibrating();
    }
    
    public boolean isNewPathExists() {
        return newPathExists;
    }

    public ArrayList<Vector3f> getCurrentPathVertices() {
        return currentRecordingPath.getVertices();
    }
    
    public float getScaleFactorX() {
        return coordConverter.getScaleFactorX();
    }

    public float getScaleFactorY() {
        return coordConverter.getScaleFactorY();
    }

    public float getCurrentYaw() {
        return currentYaw;
    }

    public Quaternion getLocalRotation() {
        return localRotation;
    }

    public Vector3f getCurrentXAxis() {
        return coordConverter.getCurrentXAxis();
    }

    public Vector3f getCurrentYAxis() {
        return coordConverter.getCurrentYAxis();
    }
    
    
    
}
