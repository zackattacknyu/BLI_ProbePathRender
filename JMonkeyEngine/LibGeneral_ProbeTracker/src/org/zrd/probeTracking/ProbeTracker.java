/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking;

import org.zrd.geometryToolkit.geometricCalculations.RotationHelper;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.nio.file.Path;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometryUtil.GeometryOutputHelper;
import org.zrd.geometryToolkit.locationTracking.LocationTracker;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.probeTracking.deviceToWorldConversion.AbstractSerialInputToWorldConverter;
import org.zrd.probeTracking.deviceToWorldConversion.SerialInputTo2DConverter;
import org.zrd.probeTracking.deviceToWorldConversion.SerialInputTo3DConverter;
import org.zrd.probeTracking.deviceToWorldConversion.SerialInputToRotated2DConverter;
import org.zrd.util.dataStreaming.ProbeDataStream;
import org.zrd.util.stats.QualityStatistics;
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
    private QualityStatistics currentQualityStats;
    private ArrayList<String> currentQualityResults;
    
    public static ProbeTracker initializeProbeTracker(
            AbstractInputSourceTracker currentSourceTracker, 
            short displacementMode, Path filePath,
            float scaleFactorX, float scaleFactorY, Vector3f startPosition){
        
        return initializeProbeTracker(currentSourceTracker,displacementMode,
                filePath,scaleFactorX,scaleFactorY,startPosition, 
                new Quaternion());
    }
    
    public static ProbeTracker initializeProbeTracker(
            AbstractInputSourceTracker currentSourceTracker, 
            short displacementMode, Path filePath,
            float scaleFactorX, float scaleFactorY, Vector3f startPosition,
            Quaternion rotationCalibration){
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
        
        coordConverter.setRotationCalibration(rotationCalibration);
        
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
        
        rotationFromData = RotationHelper.getQuaternion(currentYaw,currentPitch,currentRoll);
        
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
            currentQualityStats.addToStats(currentSourceTracker.getTrackingQuality());
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
    
    public void addendRotationCalibration(Quaternion rotation){
        Quaternion newRotationCalibration = rotation.mult(coordConverter.getRotationCalibration());
        coordConverter.setRotationCalibration(newRotationCalibration);
    }
    
    @Override
    public void startStopRecording(){
        
        if(recordingPath){
            System.out.println("Recording New Path Stopped");
            recordingText = "Press N to record a new path";
            currentRecordingPath.closeRecording();
            currentQualityResults = currentQualityStats.closeStatRecording();
            newPathExists = true;
            recordingPath = false;
        }else{
            recordingText = "Now recording new path (Press N to stop recording)";
            System.out.println("Now Recording new path");
            newPathExists = false;
            currentRecordingPath = makeNewRecorder();
            currentQualityStats = new QualityStatistics();
            recordingPath = true;
        }
        
        
    }

    public ArrayList<String> getCurrentQualityResults() {
        return currentQualityResults;
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
        return GeometryOutputHelper.getXYZDisplayString(currentPosition);
    }
    
    @Override
    public String getYawPitchRollText(){
        return GeometryOutputHelper.getYawPitchRollDisplayString(currentYaw, currentPitch, currentRoll);
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

    @Override
    public Quaternion getLocalRotation() {
        return coordConverter.getRotationCalibration().mult(rotationFromData);
    }

    @Override
    public Vector3f getCurrentDisplacement() {
        return currentDisplacement;
    }

    @Override
    public ArrayList<Vector3f> getVerticesSinceLastRead() {
        return currentRecordingPath.getMostRecentVertices();
    }

    @Override
    public float getArcLengthSinceLastRead() {
        return currentRecordingPath.getArcLengthSinceLastRead();
    }

    @Override
    public float getTrackingQuality() {
        return currentSourceTracker.getTrackingQuality();
    }

    @Override
    public void setCurrentTriangle(MeshTriangle triangle) {
    }

    
    
    
}
