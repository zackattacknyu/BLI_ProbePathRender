/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import org.zrd.probeTracking.deviceToWorldConversion.AbstractSerialInputToWorldConverter;
import org.zrd.probeTracking.deviceToWorldConversion.SerialInputTo2DConverter;
import org.zrd.probeTracking.deviceToWorldConversion.SerialInputTo3DConverter;
import org.zrd.probeTracking.deviceToWorldConversion.SerialInputToRotated2DConverter;
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
public class ProbeTrackerClient{
    
    private ProbeTracker currentTracker;

    private final Vector3f STARTING_POSITION = new Vector3f(-0.4f,-0.97f,-15.35f);
    
    private float scaleFactorX = 0.00001f,scaleFactorY = 0.00001f;
    
    private short filterMode = 0;

    private String readModeText,scaleYtext,scaleXtext,recordingText;
    
    public ProbeTrackerClient(AbstractInputSourceTracker currentSourceTracker, short displacementMode){
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
        
        currentTracker = new ProbeTracker(coordConverter,currentSourceTracker,STARTING_POSITION);
        
    }
    
    public void updateValues(){
        
        currentTracker.updateValues();
        
    }
    
    public Vector3f getCurrentNormal(){
        return currentTracker.getCurrentNormal();
    }
    
    public void updatePathRecording(){
        
        if(currentTracker.isRecordingPath()){
            System.out.println("Recording New Path Stopped");
            recordingText = "Press N to record a new path";
        }else{
            recordingText = "Now recording new path (Press N to stop recording)";
            System.out.println("Now Recording new path");
        }
        
        currentTracker.updatePathRecording();
        
    }

    public void resetProbe(){
        currentTracker.resetProbe();
    }
    
    public Vector3f getCurrentPosition(){
        return currentTracker.getCurrentPosition();
    }
    
    public void setCurrentPosition(Vector3f position){
        currentTracker.setCurrentPosition(position);
        
    }
    
    public String getRecordingText() {
        return recordingText;
    }
    
    public void updateProbeCalibration(){
        
        if(currentTracker.isCalibrating()){
            setReadMode();
        }else{
            readModeText = "Now Recalibrating. Press B again to stop.";
        }
        currentTracker.startStopCalibration();
    }
    
    private void setReadMode(){
        
        switch(filterMode){
            case 0:
                readModeText = "Probe Output Reading "
                        + "(Press V to change): "
                        + "Only Show Output";
                currentTracker.setFilterMode(0);
                break;
            case 1:
                readModeText = "Probe Output Reading "
                        + "(Press V to change): "
                        + "Raw Output Mode";
                currentTracker.setFilterMode(1);
                break;

            case 2:
                readModeText = "Probe Output Reading "
                        + "(Press V to change): "
                        + "Low-Pass Filter Mode";
                currentTracker.setFilterMode(3);
                break;

            case 3:
                readModeText = "Probe Output Reading "
                        + "(Press V to change): "
                        + "Mean Error as Threshold Mode";
                currentTracker.setFilterMode(2);
                break;
        }
        
        
    }
    
    public String getXYZtext(){
        return "(X,Y,Z) = (" + currentTracker.getCurrentPosition();
    }
    
    public String getYawPitchRollText(){
        return "(Yaw,Pitch,Roll) = (" + currentTracker.getCurrentYaw()*FastMath.RAD_TO_DEG + "," + 
                currentTracker.getCurrentPitch()*FastMath.RAD_TO_DEG + "," + 
                currentTracker.getCurrentRoll()*FastMath.RAD_TO_DEG + ")";
    }

    public boolean isNewPathExists() {
        return currentTracker.isNewPathExists();
    }

    public ArrayList<Vector3f> getCurrentPathVertices() {
        return currentTracker.getCurrentPathVertices();
    }
    
    public void incrementReadMode(){
        
        filterMode++;
        filterMode = (short) (filterMode%4);
        
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

    public short getReadMode() {
        return filterMode;
    }

    public Quaternion getLocalRotation() {
        return currentTracker.getLocalRotation();
    }

    public String getReadModeText() {
        return readModeText;
    }

    public Vector3f getCurrentXAxis() {
        return currentTracker.getCurrentXAxis();
    }

    public Vector3f getCurrentYAxis() {
        return currentTracker.getCurrentYAxis();
    }
    
    
    
}
