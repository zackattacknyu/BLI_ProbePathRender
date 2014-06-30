/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking;

import com.jme3.input.InputManager;
import org.zrd.serialReading.dataInterpretation.SerialDataInterpreter;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;
import org.zrd.bliProbePath.Properties_BLIProbePath;
import org.zrd.graphicsTools.geometry.meshTraversal.MeshHelper;
import org.zrd.keyboardTrackingRead.KeyboardTrackingImpl;

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
    
    private float currentX=-0.4f,currentY=-0.97f,currentZ=-15.35f;
    private final Vector3f startingPosition = new Vector3f(currentX,currentY,currentZ);
    
    private float scaleFactorX = -0.02f,scaleFactorY = 0.02f;
    
    private SerialDataInterpreter dataInterpreter;
    
    private float baselineYaw,currentYaw,
            baselinePitch = 0,currentPitch = 0,
            baselineRoll = 0, 
            currentRoll = 0;
    
    private float firstYaw=0, firstPitch = 0;
    private float firstRoll=0;
    
    private float currentDebugX = 0.0f,currentDebugY = 0.0f;
    
    private Vector3f currentNormal = new Vector3f(0,0,-1);
    
    private short readMode = 0;
    
    private Quaternion localRotation;
    private Quaternion displayRotation;
    private Vector3f localTranslation;
    
    private boolean calibratingX = false, calibratingY = false, recordingPath = false;
    private String readModeText,scaleYtext,scaleXtext,recordingText;
    
    private PathRecorder cubePath;
    
    private boolean newPathExists = false;
    
    ArrayList<Vector3f> currentPathVertices;
    
    private short displacementMode = 2;
    
    private ProbeDataWriter currentPathVertexWriter;
    
    private Path logFileParentPath,pathRecordingFilePath;
    
    private KeyboardTrackingImpl keyboardInputTracker;
    
    private Vector3f currentXAxis = new Vector3f(1,0,0);
    private Vector3f currentYAxis = new Vector3f(0,1,0);
    private Vector3f startingXAxis = new Vector3f(1,0,0);
    private Vector3f startingYAxis = new Vector3f(0,1,0);
    
    public ProbeTracker(InputManager manager){
        Properties trackerProps = Properties_BLIProbePath.getProperties();
        dataInterpreter = new SerialDataInterpreter(trackerProps);
        
        keyboardInputTracker = new KeyboardTrackingImpl(manager);
        displacementMode = Short.parseShort(
                trackerProps.getProperty("trackDisplacementMode"));
        
        //ensures 0,1, or 2 will be the value set
        if(displacementMode < 0 || displacementMode > 2){
            displacementMode = 2;
        }
        
        logFileParentPath = Paths.get("textFiles").resolve("logs");
        pathRecordingFilePath = logFileParentPath.resolve("paths");
        
    }
    
    public void updateValues(){
        
        dataInterpreter.updateData();
        
        baselineYaw = keyboardInputTracker.getCurrentAngles().getCurrentYaw();
        baselineRoll = keyboardInputTracker.getCurrentAngles().getCurrentRoll();
        baselinePitch = keyboardInputTracker.getCurrentAngles().getCurrentPitch();
        
        //use this style for displaying the rotation
        if(dataInterpreter.isCalibrated() && readMode > 0){
            currentYaw = dataInterpreter.getOutputYawRadians() + baselineYaw - firstYaw;
        }else{
            currentYaw = baselineYaw - firstYaw;
        }
        
        if(dataInterpreter.isCalibrated() && readMode > 0){
            currentPitch = dataInterpreter.getOutputYawRadians() + baselinePitch - firstPitch;
        }else{
            currentPitch = baselinePitch - firstPitch;
        }
        
        if(dataInterpreter.isCalibrated() && readMode > 0){
            currentRoll = dataInterpreter.getOutputYawRadians() + baselineRoll - firstRoll;
        }else{
            currentRoll = baselineRoll - firstRoll;
        }
        
        localRotation = TrackingHelper.getQuarternion(
                currentYaw,currentPitch,currentRoll);
        displayRotation = TrackingHelper.getQuarternion(
                currentYaw+firstYaw, 
                currentPitch+firstPitch, 
                currentRoll+firstRoll);
        
        float currentManualDeltaX =keyboardInputTracker.getCurrentPosChange().getXDisp();
        float currentManualDeltaY=keyboardInputTracker.getCurrentPosChange().getYDisp();
        
        Vector2f currentXYDisp = new Vector2f(
                -dataInterpreter.getDeltaX() + currentManualDeltaX,
                -dataInterpreter.getDeltaY() + currentManualDeltaY);
        Vector3f currentDisp = new Vector3f(0,0,0);
        
        currentXYDisp = TrackingHelper.scaleXYDisplacement(
                currentXYDisp, scaleFactorX, scaleFactorY);
        
        //gets x,y if there was no rotation change to it
        currentDebugX = currentDebugX + currentXYDisp.getX();
        currentDebugY = currentDebugY + currentXYDisp.getY();
        
        currentXAxis = localRotation.mult(startingXAxis);
        currentYAxis = localRotation.mult(startingYAxis);
        
        switch(displacementMode){
            
                //only use X,Y
            case 0:
                currentDisp = new Vector3f(currentXYDisp.getX(),
                        currentXYDisp.getY(),0.0f);
            break;

                // use X,Y and Yaw
            case 1:
                currentDisp = TrackingHelper.getXYDisplacement(
                        currentXYDisp.getX(),
                        currentXYDisp.getY(),
                        currentYaw);
            break;

                //use X,Y and Yaw, Pitch, Roll
            case 2:
                /*currentDisp = TrackingHelper.getXYZDisplacement(
                        currentXYDisp.getX(),
                        currentXYDisp.getY(), 
                        localRotation);*/
                currentDisp = TrackingHelper.getDisplacement(currentXYDisp.getX(), 
                        currentXYDisp.getY(), currentXAxis, currentYAxis);
                /*currentDisp = TrackingHelper.getXYZDisplacement(
                        currentXYDisp.getX(),
                        currentXYDisp.getY(), 
                        currentNormal,
                        localRotation)*/;
            break;
        
        }
        
        currentX = currentX + currentDisp.getX();
        currentY = currentY + currentDisp.getY();
        currentZ = currentZ + currentDisp.getZ();
        
        localTranslation = new Vector3f(currentX,currentY,currentZ);

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
        
        if(calibratingY){
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

        calibratingY = !calibratingY;
        
    }

    public Vector3f getCurrentXAxis() {
        return currentXAxis;
    }

    public Vector3f getCurrentYAxis() {
        return currentYAxis;
    }
    
    public Vector3f getCurrentNormal(){
        return currentNormal;
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
            cubePath = new PathRecorder(currentX,currentY,currentZ);
        }

        calibratingX = !calibratingX;
        
        
    }
    
    public void updatePathRecording(){
        try {
            if(recordingPath){
                currentPathVertexWriter.closeWriter();
                currentPathVertexWriter = null;
                dataInterpreter.startStopRecording(pathRecordingFilePath);
                
            }else{   
                String currentTimestamp = ProbeDataHelper.getTimestampSuffix();
                currentPathVertexWriter = new ProbeDataWriter(
                        pathRecordingFilePath,"pathVertices",currentTimestamp);
                dataInterpreter.startStopRecording(pathRecordingFilePath);
                
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
            cubePath = new PathRecorder(currentX,currentY,currentZ);
            System.out.println("Now Recording new path");
            recordingPath = true;
        }
        
        
    }

    public void resetProbe(){
        
        setCurrentPosition(startingPosition);
        setBaselineRotation(firstYaw,firstPitch,firstRoll);
        
    }
    
    public Vector3f getCurrentPosition(){
        return new Vector3f(currentX,currentY,currentZ);
    }
    
    public void setCurrentPosition(Vector3f position){
        currentX = position.getX();
        currentY = position.getY();
        currentZ = position.getZ();
        
    }
    
    public void setBaselineRotation(float yaw, float pitch, float roll){
        baselineYaw = yaw;
        baselinePitch = pitch;
        baselineRoll = roll;
    }
    
    public void setNormal(Vector3f normal){
        currentNormal = normal;
        reAdjustXYAxis();
    }
    private void reAdjustXYAxis(){
        Vector3f xVector = new Vector3f(1,0,0);
        startingXAxis = MeshHelper.getVectorProjOnPlane(currentNormal, xVector);
        startingXAxis.normalizeLocal();
        startingYAxis = currentNormal.cross(startingXAxis);
        startingYAxis.normalizeLocal();
    }
    
    public void setBaselineRotation(Quaternion rotation, Vector3f normal){
        setBaselineRotation(rotation);
        currentNormal = normal;
    }
    
    public void setBaselineRotation(Quaternion rotation){
        setBaselineRotation(TrackingHelper.getYaw(rotation),
                TrackingHelper.getPitch(rotation),
                TrackingHelper.getRoll(rotation));        
    }
    
    public void setBaselineRotation(Quaternion rotation, Vector3f normal, float extraAngle){
        setBaselineRotation(rotation,normal);
        baselinePitch = baselinePitch + extraAngle;
    }

    public float getCurrentZ() {
        return currentZ;
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
                dataInterpreter.setFilterMode(0);
                break;
            case 1:
                readModeText = "Probe Output Reading "
                        + "(Press V to change): "
                        + "Raw Output Mode";
                dataInterpreter.setFilterMode(1);
                break;

            case 2:
                readModeText = "Probe Output Reading "
                        + "(Press V to change): "
                        + "Low-Pass Filter Mode";
                dataInterpreter.setFilterMode(3);
                break;

            case 3:
                readModeText = "Probe Output Reading "
                        + "(Press V to change): "
                        + "Mean Error as Threshold Mode";
                dataInterpreter.setFilterMode(2);
                break;
        }
        
        
    }
    
    public String getXYZtext(){
        return "(X,Y,Z) = (" + currentX + "," + currentY + "," + currentZ + ")";
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

    public Quaternion getDisplayRotation() {
        return displayRotation;
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
