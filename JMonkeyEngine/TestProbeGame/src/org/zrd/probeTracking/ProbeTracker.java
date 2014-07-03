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
import com.jme3.math.Vector3f;
import java.io.IOException;
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
import org.zrd.util.timeTools.TimeHelper;

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
    
    private boolean calibratingX = false, calibratingY = false, recordingPath = false;
    private String readModeText,scaleYtext,scaleXtext,recordingText;
    
    private PathRecorder cubePath;
    
    private boolean newPathExists = false;
    
    ArrayList<Vector3f> currentPathVertices;
    
    private short displacementMode = 2;
    
    private float currentDeltaX=0,currentDeltaY=0;
    
    private ProbeDataWriter currentPathVertexWriter;
    
    private Path logFileParentPath,pathRecordingFilePath;

    private AbstractInputSourceTracker currentSourceTracker;
    
    //set to true if using the keyboard. if using the serial, set to false
    private boolean debugTracking = true;
    
    private AbstractSerialInputToWorldConverter coordConverter;
    
    public ProbeTracker(InputManager manager){
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
        
        currentPosition = STARTING_POSITION;
        
        if(currentSourceTracker.canBeginTracking()){
            currentYaw = currentSourceTracker.getCurrentYawRadians();
            currentPitch = currentSourceTracker.getCurrentPitchRadians();
            currentRoll = currentSourceTracker.getCurrentRollRadians();
            
            currentDeltaX = currentSourceTracker.getDeltaX();
            currentDeltaY = currentSourceTracker.getDeltaY();
        }
        
        localRotation = TrackingHelper.getQuaternion(currentYaw,currentPitch,currentRoll);
        
        Vector3f currentDisp = coordConverter.getXYZDisplacement(
                currentDeltaX, currentDeltaY, 
                currentYaw, currentPitch, currentRoll);
        
        currentPosition.addLocal(currentDisp);

        //here we record the xyz path
        if(recordingPath || calibratingX || calibratingY){
            cubePath.addToPath(currentDisp);
        }
        
        if(recordingPath){
            try {

                currentPathVertexWriter.writeLine(cubePath.getLastX() + "," + 
                                                  cubePath.getLastY() + "," + 
                                                  cubePath.getLastZ());
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
        
    }
    
    public void updateYcalibration(){
        
        /*if(calibratingY){
            float lastY = cubePath.getLastY()- cubePath.getFirstY();
            float realLastY = 8.0f*scaleFactorY;
            scaleFactorY = realLastY/lastY;
            scaleYtext = "Virtual Y to real Y scale factor "
                    + "(Press Y to recalibrate): "
                    + scaleFactorY;
        }else{
            scaleYtext= "Now calibrating. Press Y has been moved 8 units up ";
            cubePath = new PathRecorder(currentX,currentY,currentZ);
        }

        calibratingY = !calibratingY;*/
        
    }
    
    public Vector3f getCurrentNormal(){
        return currentNormal;
    }
    
    public void updateXcalibration(){
        
        /*if(calibratingX){
            float lastX = cubePath.getLastX() - cubePath.getFirstX();
            float realLastX = 8.0f*scaleFactorX;
            scaleFactorX = realLastX/lastX;
            scaleXtext = "Virtual X to real X scale factor "
                    + "(Press X to recalibrate): "
                    + scaleFactorX;
        }else{
            scaleXtext="Now calibrating. Press X has been moved 8 units right ";
            cubePath = new PathRecorder(currentX,currentY,currentZ);
        }

        calibratingX = !calibratingX;*/
        
        
    }
    
    public void updatePathRecording(){
        try {
            if(recordingPath){
                currentPathVertexWriter.closeWriter();
                currentPathVertexWriter = null;
                //dataInterpreter.startStopRecording(pathRecordingFilePath);
                
            }else{   
                String currentTimestamp = TimeHelper.getTimestampSuffix();
                currentPathVertexWriter = new ProbeDataWriter(
                        pathRecordingFilePath,"pathVertices",currentTimestamp);
                //dataInterpreter.startStopRecording(pathRecordingFilePath);
                
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
        if(recordingPath){
            System.out.println("Recording New Path Stopped");
            currentPathVertices = cubePath.toLineVertices();
            newPathExists = true;
            recordingText = "Press N to record a new path";
            recordingPath = false;
        }else{
            newPathExists = false;
            recordingText = "Now recording new path (Press N to stop recording)";
            cubePath = new PathRecorder(currentPosition);
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
        return currentPathVertices;
    }
    
    public Vector3f getFirstPathVertex(){
        return currentPathVertices.get(0);
    }
    
    public Vector3f getLastPathVertex(){
        return currentPathVertices.get(currentPathVertices.size()-1);
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
