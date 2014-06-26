/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialReading.dataFilter;

import org.zrd.serialReading.dataFilter.SerialDataCalibration;
import org.zrd.serialReading.dataFilter.OrientationFilterThreshold;
import org.zrd.serialReading.dataFilter.OrientationFilterRaw;
import org.zrd.serialReading.dataFilter.OrientationFilterLowPass;
import org.zrd.serialReading.dataFilter.OrientationFilter;
import org.zrd.serialReading.dataReader.SerialDataReader;

/**
 * 
 * @author BLI
 */
public class SerialDataFilter {
    
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
    private String currentSerialOutput;
    
    private boolean calibrating = false;
    private boolean calibrated = false;
    
    private float meanErrorPitch = 0;
    private float meanErrorYaw = 0;
    private float meanErrorRoll = 0;
    
    /* This tells the filter to be used
     *    0: Do not output changes
     *    1: Output raw angles
     *    2: Output angles with thresholded changes
     *    3: Output low pass filter angles
     */
    private int filterMode;
    
    private SerialDataCalibration currentCalib;
    private OrientationFilter orientationFilterRaw;
    private OrientationFilter orientationFilterThreshold;
    private OrientationFilter orientationFilterLowPass;

    public SerialDataFilter() {
        serial = new SerialDataReader();
        System.out.println("Waiting to receive input...");
    }

    public void setFilterMode(int filterMode) {
        this.filterMode = filterMode;
    }

    public String getCurrentSerialOutput() {
        return serial.getCurrentSerialOutput();
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
        
        orientationFilterRaw = new OrientationFilterRaw(
                firstPitch,firstYaw,firstRoll);
        orientationFilterThreshold = new OrientationFilterThreshold(
                firstPitch,firstYaw,firstRoll,
                meanErrorPitch,meanErrorYaw,meanErrorRoll);
        orientationFilterLowPass = new OrientationFilterLowPass(
                firstPitch,firstYaw,firstRoll);
        

    }

    public void updateData(){
        
        serial.updateData();
        processArdData();
    }
    
    private void processObjectUpdate(){
        
        processYawPitchRoll();
        processXYdata();

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
    
    private void processXYdata(){
        deltaX = serial.getDeltaX();
        deltaY = serial.getDeltaY();
    }
    
    private void addCurrentDataToFilter(OrientationFilter filter){
        filter.addDataToFilter(
                serial.getCurrentPitch(), 
                serial.getCurrentYaw(), 
                serial.getCurrentRoll());
    }
    
    private void setDataUsingFilter(OrientationFilter filter){
        currentPitch = filter.getOutputPitch();
        currentRoll = filter.getOutputRoll();
        currentYaw = filter.getOutputYaw();
    }
    
    private void processYawPitchRoll(){
        
        addCurrentDataToFilter(orientationFilterRaw);
        addCurrentDataToFilter(orientationFilterThreshold);
        addCurrentDataToFilter(orientationFilterLowPass);
        
        switch(filterMode){
            case 1:
                setDataUsingFilter(orientationFilterRaw);
                break;
                
            case 2:
                setDataUsingFilter(orientationFilterThreshold);
                break;
                
            case 3:
                setDataUsingFilter(orientationFilterLowPass);
                break;
        }
    }

    public boolean isCalibrated() {
        return calibrated;
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
