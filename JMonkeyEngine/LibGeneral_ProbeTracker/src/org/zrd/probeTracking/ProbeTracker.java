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
import org.zrd.geometryToolkit.geometryUtil.GeneralHelper;
import org.zrd.geometryToolkit.locationTracking.LocationTracker;
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
public class ProbeTracker implements ProbeDataStream, LocationTracker{

    private final Vector3f startingPosition;
    
    private float currentYaw=0,currentPitch = 0,currentRoll = 0;
    
    private Quaternion rotationFromData;
    private Quaternion rotationCalibration = new Quaternion();
    
    private Vector3f currentPosition;
    private Vector3f currentDisplacement;
    private Vector3f displacementSinceLastPoint = new Vector3f();
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
    
    public void resetProbeReader(){
        currentSourceTracker.resetProbeReader();
    }
    
    @Override
    public void updateData(){
        
        currentSourceTracker.updateData();
        
        currentYaw = currentSourceTracker.getCurrentYawRadians();
        currentPitch = currentSourceTracker.getCurrentPitchRadians();
        currentRoll = currentSourceTracker.getCurrentRollRadians();

        currentDeltaX = currentSourceTracker.getDeltaX();
        currentDeltaY = currentSourceTracker.getDeltaY();
        
        rotationFromData = TrackingHelper.getQuaternion(currentYaw,currentPitch,currentRoll);
        
        //gets the current displacement vector
        currentDisplacement = coordConverter.getXYZDisplacement(
                currentDeltaX, currentDeltaY, 
                currentYaw, currentPitch, currentRoll);
        
        //adds the displacement to current position
        currentPosition.addLocal(currentDisplacement.clone());
        displacementSinceLastPoint.addLocal(currentDisplacement.clone());
        
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
    
    @Override
    public Vector3f getDisplacementSinceLastPoint(){
        return displacementSinceLastPoint;
    }
    @Override
    public void resetDisplacementSinceLastPoint(){
        displacementSinceLastPoint = new Vector3f();
    }
    
    /**
     * This multiplies the given scale factor by the current ones
     *      and uses the result as the operant scale factor
     * @param scaleFactor 
     */
    public void rescaleCoordinates(float scaleFactor){
        float scaleX = coordConverter.getScaleFactorX();
        float scaleY = coordConverter.getScaleFactorY();
        coordConverter.setScaleFactorX(scaleFactor*scaleX);
        coordConverter.setScaleFactorY(scaleFactor*scaleY);
    }
    
    public void setRotationCalibration(Quaternion rotation){
        rotationCalibration = rotation.clone();
        coordConverter.setRotationCalibrationMatrix(rotation.toRotationMatrix());
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
    
    public ArrayList<Vector3f> lastRecordedPathVertices(){
        return currentRecordingPath.getVertices();
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

    @Override
    public boolean isRecordingPath() {
        return recordingPath;
    }
    
    @Override
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
    
    @Override
    public String getXYZtext(){
        return GeneralHelper.getXYZDisplayString(currentPosition);
    }
    
    @Override
    public String getYawPitchRollText(){
        return GeneralHelper.getYawPitchRollDisplayString(currentYaw, currentPitch, currentRoll);
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
        return rotationCalibration.mult(rotationFromData);
    }

    @Override
    public Vector3f getCurrentDisplacement() {
        return currentDisplacement;
    }

    
    
    
}
