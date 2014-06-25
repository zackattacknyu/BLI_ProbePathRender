/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataInterpretation;

import dataFilter.SerialDataFilter;

/**
 * 
 * @author BLI
 */
public class SerialDataInterpreter {
    
    private SerialDataFilter serial;

    float deltaX = 0;
    float deltaY = 0;
    private float outputYawRadians,outputPitchRadians,outputRollRadians;


    public SerialDataInterpreter() {
        serial = new SerialDataFilter();
        System.out.println("Waiting to receive input...");
    }

    public void updateData(){
        serial.updateData();
        processYawPitchRoll();
        processXYdata();
    }
    
    private void processXYdata(){
        deltaX = serial.getDeltaX();
        deltaY = serial.getDeltaY();
        
        deltaX = getOutputDisp(deltaX);
        deltaY = getOutputDisp(deltaY);
    }
    
    private void processYawPitchRoll(){
        outputYawRadians = getOutputAngle(serial.getCurrentYaw());
        outputRollRadians = getOutputAngle(serial.getCurrentRoll());
        outputPitchRadians = getOutputAngle(serial.getCurrentPitch());
    }
    
    /**
     * This is the method that takes in an angle from the probe
     *      and returns the desired angle to the tracker
     * @param angle
     * @return 
     */
    private float getOutputAngle(float angle){
        return SerialDataHelper.getReturnAngle(angle);
    }
    
    /**
     * This is the method that takes in an x or y value from the probe
     *      and returns the desired displacement to the tracker
     * @param disp
     * @return 
     */
    private float getOutputDisp(float disp){
        return SerialDataHelper.getReturnDisp(disp);
    }

    public boolean isCalibrated() {
        return serial.isCalibrated();
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
        serial.startStopCalibration();
    }
    
    public void setFilterMode(int mode){
        serial.setFilterMode(mode);
    }
    

    public boolean isCalibrating() {
        return serial.isCalibrating();
    }
    
    public float getDeltaX() {
        return deltaX;
    }

    public float getDeltaY() {
        return deltaY;
    }

    public String getCurrentSerialOutput() {
        return serial.getCurrentSerialOutput();
    }
   
}
