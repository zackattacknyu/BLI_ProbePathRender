/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking;

import com.jme3.math.FastMath;
import org.zrd.probeTracking.deviceToWorldConversion.TrackingHelper;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.nio.file.Path;
import java.util.ArrayList;
import org.zrd.probeTracking.deviceToWorldConversion.AbstractSerialInputToWorldConverter;
import org.zrd.probeTracking.deviceToWorldConversion.SerialInputTo2DConverter;
import org.zrd.probeTracking.deviceToWorldConversion.SerialInputTo3DConverter;
import org.zrd.probeTracking.deviceToWorldConversion.SerialInputToRotated2DConverter;
import org.zrd.util.dataStreaming.ProbeDataStream;
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
public class ProbeTracker implements ProbeDataStream{

    private final Vector3f startingPosition;
    
    private float currentYaw=0,currentPitch = 0,currentRoll = 0;
    
    private Quaternion localRotation;
    private Vector3f currentPosition;
    private Vector2f currentXYPosition;
    
    private boolean recordingPath = false;
    
    private PathRecorder currentRecordingPath;
    private Path pathRecordingFilePath = null;
    
    private boolean newPathExists = false;
    private float currentDeltaX=0,currentDeltaY=0;
    private AbstractInputSourceTracker currentSourceTracker;
    private AbstractSerialInputToWorldConverter coordConverter;
    private String recordingText;
    
    public static ProbeTracker initializeProbeTracker(
            AbstractInputSourceTracker currentSourceTracker, 
            short displacementMode, Path filePath,
            float scaleFactorX, float scaleFactorY, Vector3f startPosition){
        AbstractSerialInputToWorldConverter coordConverter;
        
        //default option for coord conversion
        coordConverter = new SerialInputTo3DConverter();
        
        //other options if specified
        switch(displacementMode){
            case 0:
                coordConverter = new SerialInputTo2DConverter();
                break;
                
            case 1:
                coordConverter = new SerialInputToRotated2DConverter();
                break;
        }
        
        coordConverter.setScaleFactorX(scaleFactorX);
        coordConverter.setScaleFactorY(scaleFactorY);
        
        return new ProbeTracker(coordConverter,currentSourceTracker,
                startPosition,filePath);
    }
    
    public ProbeTracker(AbstractSerialInputToWorldConverter coordConvertor, 
            AbstractInputSourceTracker currentSourceTracker,
            Vector3f startingPosition, Path filePath){
        this.coordConverter = coordConvertor;
        this.currentSourceTracker = currentSourceTracker;
        
        this.startingPosition = startingPosition;
        
        currentPosition = startingPosition.clone();
        
        currentXYPosition = new Vector2f(startingPosition.getX(),startingPosition.getY());
        
        pathRecordingFilePath = filePath;
        
    }
    
    @Override
    public void updateData(){
        
        currentSourceTracker.updateData();
        
        currentYaw = currentSourceTracker.getCurrentYawRadians();
        currentPitch = currentSourceTracker.getCurrentPitchRadians();
        currentRoll = currentSourceTracker.getCurrentRollRadians();

        currentDeltaX = currentSourceTracker.getDeltaX();
        currentDeltaY = currentSourceTracker.getDeltaY();
        
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
    
    @Override
    public void startStopRecording(){
        
        if(recordingPath){
            System.out.println("Recording New Path Stopped");
            recordingText = "Press N to record a new path";
            currentRecordingPath.closeRecording();
            newPathExists = true;
            recordingPath = false;
        }else{
            recordingText = "Now recording new path (Press N to stop recording)";
            System.out.println("Now Recording new path");
            newPathExists = false;
            currentRecordingPath = makeNewRecorder();
            recordingPath = true;
        }
        
        
    }

    public String getRecordingText() {
        return recordingText;
    }
    
    private PathRecorder makeNewRecorder(){
        if(pathRecordingFilePath == null){
            return new PathRecorder(currentPosition);
        }else{
            return new PathRecorder(currentPosition,pathRecordingFilePath);
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
    
    public String getXYZtext(){
        return "(X,Y,Z) = " + currentPosition;
    }
    
    public String getYawPitchRollText(){
        return "(Yaw,Pitch,Roll) = (" + currentYaw*FastMath.RAD_TO_DEG + "," + 
                currentPitch*FastMath.RAD_TO_DEG + "," + 
                currentRoll*FastMath.RAD_TO_DEG + ")";
    }
    
    public float getCurrentYaw() {
        return currentYaw;
    }

    public float getCurrentPitch() {
        return currentPitch;
    }

    public float getCurrentRoll() {
        return currentRoll;
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
