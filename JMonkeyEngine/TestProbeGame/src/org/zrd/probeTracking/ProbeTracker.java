/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking;

import org.zrd.probeTracking.deviceToWorldConversion.TrackingHelper;
import org.zrd.util.dataWriting.ProbeDataWriter;
import com.jme3.input.InputManager;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;
import org.zrd.bliProbePath.Properties_BLIProbePath;
import org.zrd.probeTracking.deviceToWorldConversion.AbstractSerialInputToWorldConverter;
import org.zrd.probeTracking.deviceToWorldConversion.SerialInputTo2DConverter;
import org.zrd.probeTracking.deviceToWorldConversion.SerialInputTo3DConverter;
import org.zrd.probeTracking.deviceToWorldConversion.SerialInputToRotated2DConverter;
import org.zrd.probeTracking.probeTrackers.AbstractInputSourceTracker;
import org.zrd.probeTracking.probeTrackers.KeyboardInputSourceTracker;
import org.zrd.probeTracking.probeTrackers.SerialInputSourceTracker;

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

    private final Vector3f STARTING_POSITION = new Vector3f(-0.4f,-0.97f,-15.35f);
    
    private float scaleFactorX = -0.00001f,scaleFactorY = 0.00001f;
    
    private float currentYaw=0,currentPitch = 0,currentRoll = 0;
    
    private Vector3f currentNormal = new Vector3f(0,0,-1);
    
    private short readMode = 0;
    
    private Quaternion localRotation;
    private Vector3f currentPosition;
    private Vector2f currentXYPosition;
    
    private boolean recordingPath = false;
    private String readModeText,scaleYtext,scaleXtext,recordingText;
    
    private PathRecorder currentRecordingPath;
    
    private boolean newPathExists = false;
    
    private short displacementMode = 2;
    
    private float currentDeltaX=0,currentDeltaY=0;
    
    private ProbeDataWriter currentPathVertexWriter;
    
    private Path logFileParentPath,pathRecordingFilePath;

    private AbstractInputSourceTracker currentSourceTracker;
    
    //set to true if using the keyboard. if using the serial, set to false
    private boolean debugTracking = true;
    
    private AbstractSerialInputToWorldConverter coordConverter;
    
    public ProbeTracker(InputManager manager){
        currentPosition = new Vector3f(0,0,0);
        currentPosition.addLocal(STARTING_POSITION);
        
        currentXYPosition = new Vector2f(0,0);
        currentXYPosition.addLocal(STARTING_POSITION.getX(), STARTING_POSITION.getY());
        
        Properties trackerProps = Properties_BLIProbePath.getProperties();
        
        if(debugTracking){
            currentSourceTracker = new KeyboardInputSourceTracker(manager);
        }else{
            currentSourceTracker = new SerialInputSourceTracker(trackerProps);
        }

        displacementMode = Short.parseShort(
                trackerProps.getProperty("trackDisplacementMode"));
        
        //ensures 0,1, or 2 will be the value set
        if(displacementMode < 0 || displacementMode > 2){
            displacementMode = 2;
        }
        
        switch(displacementMode){
            case 0:
                coordConverter = new SerialInputTo2DConverter();
                break;
                
            case 1:
                coordConverter = new SerialInputToRotated2DConverter();
                break;
                
            case 2:
                coordConverter = new SerialInputTo3DConverter();
                break;
        }
        
        coordConverter.setScaleFactorX(scaleFactorX);
        coordConverter.setScaleFactorY(scaleFactorY);
        
        logFileParentPath = Paths.get("textFiles").resolve("logs");
        pathRecordingFilePath = logFileParentPath.resolve("paths");
        
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
        
        currentXYPosition.addLocal(currentDeltaX*scaleFactorX, currentDeltaY*scaleFactorY);

        //here we record the xyz path
        if(recordingPath){
            currentRecordingPath.addToPath(currentPosition,currentXYPosition,
                    currentYaw, currentPitch, currentRoll);
        }
        
    }
    
    
    
    public void updateYcalibration(){
        
        /*if(calibratingY){
            float lastY = currentRecordingPath.getLastY()- currentRecordingPath.getFirstY();
            float realLastY = 8.0f*scaleFactorY;
            scaleFactorY = realLastY/lastY;
            scaleYtext = "Virtual Y to real Y scale factor "
                    + "(Press Y to recalibrate): "
                    + scaleFactorY;
        }else{
            scaleYtext= "Now calibrating. Press Y has been moved 8 units up ";
            currentRecordingPath = new PathRecorder(currentX,currentY,currentZ);
        }

        calibratingY = !calibratingY;*/
        
    }
    
    public Vector3f getCurrentNormal(){
        return currentNormal;
    }
    
    public void updateXcalibration(){
        
        /*if(calibratingX){
            float lastX = currentRecordingPath.getLastX() - currentRecordingPath.getFirstX();
            float realLastX = 8.0f*scaleFactorX;
            scaleFactorX = realLastX/lastX;
            scaleXtext = "Virtual X to real X scale factor "
                    + "(Press X to recalibrate): "
                    + scaleFactorX;
        }else{
            scaleXtext="Now calibrating. Press X has been moved 8 units right ";
            currentRecordingPath = new PathRecorder(currentX,currentY,currentZ);
        }

        calibratingX = !calibratingX;*/
        
        
    }
    
    public void updatePathRecording(){
        
        if(recordingPath){
            currentRecordingPath.closeRecording();
            System.out.println("Recording New Path Stopped");
            newPathExists = true;
            recordingText = "Press N to record a new path";
            recordingPath = false;
        }else{
            newPathExists = false;
            recordingText = "Now recording new path (Press N to stop recording)";
            currentRecordingPath = new PathRecorder(currentPosition);
            System.out.println("Now Recording new path");
            recordingPath = true;
        }
        
        
    }

    public void resetProbe(){
        
        setCurrentPosition(STARTING_POSITION);
        setRotation(0,0,0);
        
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
    
    public String getRecordingText() {
        return recordingText;
    }
    
    public void updateProbeCalibration(){
        
        if(currentSourceTracker.isCalibrating()){

            setReadMode();
            currentSourceTracker.startStopCalibration();

        }else{
            readModeText = "Now Recalibrating. Press B again to stop.";
            currentSourceTracker.startStopCalibration();
        }
        
    }
    
    private void setReadMode(){
        
        switch(readMode){
            case 0:
                readModeText = "Probe Output Reading "
                        + "(Press V to change): "
                        + "Only Show Output";
                currentSourceTracker.setFilterMode(0);
                break;
            case 1:
                readModeText = "Probe Output Reading "
                        + "(Press V to change): "
                        + "Raw Output Mode";
                currentSourceTracker.setFilterMode(1);
                break;

            case 2:
                readModeText = "Probe Output Reading "
                        + "(Press V to change): "
                        + "Low-Pass Filter Mode";
                currentSourceTracker.setFilterMode(3);
                break;

            case 3:
                readModeText = "Probe Output Reading "
                        + "(Press V to change): "
                        + "Mean Error as Threshold Mode";
                currentSourceTracker.setFilterMode(2);
                break;
        }
        
        
    }
    
    public String getXYZtext(){
        return "(X,Y,Z) = (" + currentPosition;
    }
    
    public String getYawPitchRollText(){
        return "(Yaw,Pitch,Roll) = (" + currentYaw*FastMath.RAD_TO_DEG + "," + 
                currentPitch*FastMath.RAD_TO_DEG + "," + 
                currentRoll*FastMath.RAD_TO_DEG + ")";
    }

    public boolean isNewPathExists() {
        return newPathExists;
    }

    public ArrayList<Vector3f> getCurrentPathVertices() {
        return currentRecordingPath.getVertices();
    }
    
    public void incrementReadMode(){
        
        readMode++;
        readMode = (short) (readMode%4);
        
        setReadMode();  
        
    }

    public String getScaleYtext() {
        return scaleYtext;
    }

    public String getScaleXtext() {
        return scaleXtext;
    }

    public float getScaleFactorX() {
        return scaleFactorX;
    }

    public float getScaleFactorY() {
        return scaleFactorY;
    }

    public float getCurrentYaw() {
        return currentYaw;
    }

    public short getReadMode() {
        return readMode;
    }

    public Quaternion getLocalRotation() {
        return localRotation;
    }

    public String getReadModeText() {
        return readModeText;
    }

    public Vector3f getCurrentXAxis() {
        return coordConverter.getCurrentXAxis();
    }

    public Vector3f getCurrentYAxis() {
        return coordConverter.getCurrentYAxis();
    }
    
    
    
}
