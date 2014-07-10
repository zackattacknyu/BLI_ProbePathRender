/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataFilter;

import java.util.Properties;
import org.zrd.serialDataInterpreter.dataReader.SerialDataReader;

/**
 * 
 * @author BLI
 */
public class SerialDataFilter {
    
    private SerialDataReader serial;
    
    float deltaX = 0;
    float deltaY = 0;
    private float currentYaw=0,currentPitch=0,currentRoll=0;
    
    private boolean calibrating = false;
    private boolean calibrated = false;
    
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

    public SerialDataFilter(Properties props) {
        serial = new SerialDataReader(props);
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
        
        currentPitch = currentCalib.getMeanPitch();
        currentRoll = currentCalib.getMeanRoll();
        currentYaw = currentCalib.getMeanYaw();
        
        orientationFilterRaw = new OrientationFilterRaw(
                currentPitch,currentYaw,currentRoll);
        orientationFilterThreshold = new OrientationFilterThreshold(
                currentPitch,
                currentYaw,
                currentRoll,
                currentCalib.getMeanErrorPitch(),
                currentCalib.getMeanErrorYaw(),
                currentCalib.getMeanErrorRoll());
        orientationFilterLowPass = new OrientationFilterLowPass(
                currentPitch,currentYaw,currentRoll);
        

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
