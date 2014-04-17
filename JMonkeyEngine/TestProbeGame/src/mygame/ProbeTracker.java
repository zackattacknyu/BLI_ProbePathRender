/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;

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
    
    private float currentX,currentY;
    
    private float currentManualDeltaX,currentManualDeltaY;
    
    private float scaleFactorX = -0.02f,scaleFactorY = 0.02f;
    
    private ArduinoDataInterpreter dataInterpreter;
    
    private float baselineYaw,currentYaw,baselinePitch,currentPitch;
    
    private short readMode = 0;
    
    private Quaternion localRotation;
    private Vector3f localTranslation;
    
    private boolean calibratingX = false, calibratingY = false, recordingPath = false;
    private String readModeText,scaleYtext,scaleXtext,recordingText;
    
    private PathRecorder cubePath;
    
    private boolean newPathExists = false;
    
    ArrayList<Vector3f> currentPathVertices;
    
    public ProbeTracker(){
        
        dataInterpreter = new ArduinoDataInterpreter();
        
    }
    
    public void updateValues(){
        
        dataInterpreter.updateData();
        
        //use this style for displaying the rotation
        if(dataInterpreter.isCalibrated() && readMode > 0){
            currentYaw = dataInterpreter.getOutputYawRadians() + baselineYaw;
        }else{
            currentYaw = baselineYaw;
        }
        
        localRotation = LineHelper.getQuarternion(currentYaw);
        
        //littleObject.setLocalRotation(LineHelper.getQuarternion(currentYaw));
        
        boolean useYaw = true;
        
        Vector2f currentDisp;
        
        float currentDeltaX = -dataInterpreter.getDeltaX() + currentManualDeltaX;
        float currentDeltaY = -dataInterpreter.getDeltaY() + currentManualDeltaY;
        
        if(useYaw){
            currentDisp = LineHelper.getXYDisplacement(currentDeltaX,currentDeltaY,currentYaw);
        }else{
            currentDisp = new Vector2f(currentDeltaX,currentDeltaY);
        }
        
        currentDisp = LineHelper.scaleDisplacement(currentDisp, scaleFactorX, scaleFactorY);
        
        currentManualDeltaX = 0;
        currentManualDeltaY = 0;
        
        currentX = currentX + currentDisp.getX();
        currentY = currentY + currentDisp.getY();
        
        localTranslation = new Vector3f(currentX,currentY,0.0f);
        
        //littleObject.setLocalTranslation(currentX, currentY, 0.0f);
        
        /*
         * Around here is where we will want to record the xy path
         */
        if(recordingPath || calibratingX || calibratingY){
            //cubePath.addToPath(dataInterpreter.getDeltaX(), -dataInterpreter.getDeltaY());
            cubePath.addToPath(currentDisp);
        }
        
    }
    
    public void updateYcalibration(){
        
        if(calibratingY){
            float lastY = cubePath.getLastY()- cubePath.getFirstY();
            float realLastY = 8.0f*scaleFactorY;
            scaleFactorY = realLastY/lastY;
            scaleYtext = "Virtual Y to real Y scale factor "
                    + "(Press Y to recalibrate): "
                    + scaleFactorY;
        }else{
            scaleYtext= "Now calibrating. Press Y has been moved 8 units up ";
            cubePath = new PathRecorder(currentX,currentY);
        }

        calibratingY = !calibratingY;
        
    }
    
    public void updateXcalibration(){
        
        if(calibratingX){
            float lastX = cubePath.getLastX() - cubePath.getFirstX();
            float realLastX = 8.0f*scaleFactorX;
            scaleFactorX = realLastX/lastX;
            scaleXtext = "Virtual X to real X scale factor "
                    + "(Press X to recalibrate): "
                    + scaleFactorX;
        }else{
            scaleXtext="Now calibrating. Press X has been moved 8 units right ";
            cubePath = new PathRecorder(currentX,currentY);
        }

        calibratingX = !calibratingX;
        
        
    }
    
    public void updatePathRecording(){
        
        if(recordingPath){
            System.out.println("Recording New Path Stopped");
            currentPathVertices = LineHelper.convertPathRecordingToLineVertices(cubePath);
            newPathExists = true;
            recordingText = "Press N to record a new path";
            recordingPath = false;
        }else{
            newPathExists = false;
            recordingText = "Now recording new path (Press N to stop recording)";
            cubePath = new PathRecorder(currentX,currentY);
            System.out.println("Now Recording new path");
            recordingPath = true;
        }
        
        
    }

    public void resetProbe(){
        
        currentX = 0;
        currentY = 0;
        
    }
    
    public void moveUp(){
        currentManualDeltaY = 1.0f/2.0f;
    }
    
    public void moveDown(){
        currentManualDeltaY = -1.0f/2.0f;
    }
    
    public void moveLeft(){
        currentManualDeltaX = 1.0f/2.0f;
    }
    
    public void moveRight(){
        currentManualDeltaX = -1.0f/2.0f;
    }
    
    public void rotateClockwise(){
        
        baselineYaw = baselineYaw - 1.0f/20.0f;
    }
    
    public void rotateCounterClockwise(){
        
        baselineYaw = baselineYaw + 1.0f/20.0f;
    }
    
    public String getRecordingText() {
        return recordingText;
    }
    
    public void updateProbeCalibration(){
        
        if(dataInterpreter.isCalibrating()){

            setReadMode();
            dataInterpreter.startStopCalibration();

        }else{
            readModeText = "Now Recalibrating. Press B again to stop.";
            dataInterpreter.startStopCalibration();
        }
        
    }
    
    private void setReadMode(){
        
        switch(readMode){
            case 0:
                readModeText = "Probe Output Reading "
                        + "(Press V to change): "
                        + "Only Show Output";
                break;
            case 1:
                readModeText = "Probe Output Reading "
                        + "(Press V to change): "
                        + "Raw Output Mode";
                dataInterpreter.setRawSwitch(0);
                break;

            case 2:
                readModeText = "Probe Output Reading "
                        + "(Press V to change): "
                        + "Low-Pass Filter Mode";
                dataInterpreter.setUseLowPassFilterData(true);
                dataInterpreter.setRawSwitch(0);
                break;

            case 3:
                readModeText = "Probe Output Reading "
                        + "(Press V to change): "
                        + "Mean Error as Threshold Mode";
                dataInterpreter.setUseLowPassFilterData(false);
                dataInterpreter.setRawSwitch(1);
                break;
        }
        
        
    }

    public boolean isNewPathExists() {
        return newPathExists;
    }

    public ArrayList<Vector3f> getCurrentPathVertices() {
        return currentPathVertices;
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

    public float getCurrentX() {
        return currentX;
    }
    
    public float getCurrentY() {
        return currentY;
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

    public Vector3f getLocalTranslation() {
        return localTranslation;
    }

    public String getReadModeText() {
        return readModeText;
    }
    
    
    
}
