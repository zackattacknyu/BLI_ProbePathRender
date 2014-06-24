/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataInterpretation;

import dataReaderImpl.SerialReader;
import util.PropertiesHelper;
import com.jme3.math.FastMath;
import java.util.HashMap;
import java.util.Properties;
import dataInterpretation.SerialDataPoint;
import dataInterpretation.DataSet;
import dataInterpretation.LowPassFilterData;

/**
 * 
 * @author BLI
 */
public class ArduinoDataInterpreter {
    
    private SerialDataReader serial;

    float deltaXangle=0;
    float deltaYangle=0;
    float deltaZangle=0; 
    float deltaX = 0;
    float deltaY = 0;
    float deltaPitch = 0;
    float deltaRoll = 0;
    float deltaYaw = 0;
    float lastPitch = 0;
    float lastRoll = 0;
    float lastYaw = 0;
    private float currentYaw=0,currentPitch=0,currentRoll=0;
    private float firstYaw=0,firstPitch=0,firstRoll=0;
    private float outputYawRadians=0,outputPitchRadians=0,outputRollRadians=0;
    private String currentSerialOutput, previousSerialOutput;
    
    private boolean useLowPassFilterData = false;
    
    private boolean calibrating = false;
    private boolean calibrated = false;
    
    private float meanErrorPitch = 0;
    private float meanErrorYaw = 0;
    private float meanErrorRoll = 0;
    
    //set to 1 to use threshold factor
    //set to 0 to not use threshold factor
    private float rawSwitch = 1;
    
    private LowPassFilterData yawData;

    //factor to multiply mean error by before processing the change
    private float thresholdFactor = 3.0f;
    
    private SerialDataCalibration currentCalib;

    public ArduinoDataInterpreter() {
        serial = new SerialDataReader();
        System.out.println("Waiting to receive input...");
    }

    public void setRawSwitch(float rawSwitch) {
        this.rawSwitch = rawSwitch;
    }

    public void setUseLowPassFilterData(boolean useLowPassFilterData) {
        this.useLowPassFilterData = useLowPassFilterData;
    }

    public String getCurrentSerialOutput() {
        return currentSerialOutput;
    }
    
    private void processArdData(){
        
        if(serial.isUpdateExists()){
            
            if(calibrating){
                processCurrentCalibrationPoint();
            }else if(calibrated){
                processObjectUpdate();
            }
            
        }
        
        
        
    }
    
    private void processCurrentCalibrationPoint(){
        
        currentCalib.addCalibrationPoint(serial.getCurrentSerialData());
        yawData.addToData(serial.getCurrentSerialData().getYaw());
        
    }
    
    private void processCalibration(){
        
        currentCalib.finishCalibration();
        
        lastPitch = currentCalib.getMeanPitch();
        lastRoll = currentCalib.getMeanRoll();
        lastYaw = currentCalib.getMeanYaw();
        
        currentPitch = lastPitch;
        currentRoll = lastRoll;
        currentYaw = lastYaw;
        
        firstPitch = lastPitch;
        firstRoll = lastRoll;
        firstYaw = lastYaw;
        
        meanErrorPitch = currentCalib.getMeanErrorPitch();
        meanErrorRoll = currentCalib.getMeanErrorRoll();
        meanErrorYaw = currentCalib.getMeanErrorYaw();
        

    }

    public void updateData(){
        
        serial.updateData();
        processArdData();
    }
    
    private void processObjectUpdate(){
        
        processYawPitchRoll();
        processXYdata();

    }
    
    private void processXYdata(){
        deltaX = serial.getDeltaX();
        deltaY = serial.getDeltaY();
    }
    
    private void processYawPitchRoll(){
        
        float pitch = serial.getCurrentPitch();
        float roll = serial.getCurrentRoll();
        float yaw = serial.getCurrentYaw();
        
        yawData.addToData(yaw);
        
        deltaPitch = pitch - lastPitch;
        deltaRoll = roll - lastRoll;
        deltaYaw = yaw - lastYaw;

        if(Math.abs(deltaPitch) > thresholdFactor*meanErrorPitch*rawSwitch){
            currentPitch = pitch;
            outputPitchRadians = getEulerAngle(pitch-firstPitch);
        }
        if(Math.abs(deltaRoll) > thresholdFactor*meanErrorRoll*rawSwitch){
            currentRoll = roll;
            outputRollRadians = getEulerAngle(roll-firstRoll);
        }
        
        if(Math.abs(deltaYaw) > thresholdFactor*meanErrorYaw*rawSwitch){
            
            if(useLowPassFilterData){
                outputYawRadians = getEulerAngle(yawData.getAverage()-firstYaw);
            }else{
                outputYawRadians = getEulerAngle(yaw-firstYaw);
            }
            
            
            
            currentYaw = yaw;
        }
    }

    public boolean isCalibrated() {
        return calibrated;
    }
    
    
    
    //converts degrees, which the data is in, to radians, which is used
    //  by JMonkeyEngine for the rotation
    private float getEulerAngle(float degrees){
        return degrees*FastMath.DEG_TO_RAD;
    }

    public float getOutputYawRadians() {
        return outputYawRadians;
    }

    public float getOutputPitchRadians() {
        return outputPitchRadians;
    }

    public float getOutputRollRadians() {
        return outputRollRadians;
    }
    
    
    public void startStopCalibration(){
        
        calibrating = !calibrating; 
        
        if(calibrating){
            
            //start the calibration code
            currentCalib = new SerialDataCalibration();
            yawData = new LowPassFilterData(3);
            
        }else{
            
            //end the calibration
            calibrated = true;
            processCalibration();
            currentCalib.displayCalibrationResults();
        }
        
    }
    
    public float getDeltaXangle() {
        return deltaXangle;
    }

    public float getDeltaYangle() {
        return deltaYangle;
    }

    public float getDeltaZangle() {
        return deltaZangle;
    }

    public boolean isCalibrating() {
        return calibrating;
    }
    
    public float getDeltaX() {
        return deltaX;
    }

    public float getDeltaY() {
        return deltaY;
    }
   
}
