/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialReading.dataInterpretation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zrd.probeTracking.ProbeDataHelper;
import org.zrd.probeTracking.ProbeDataWriter;
import org.zrd.serialReading.dataFilter.SerialDataFilter;

/**
 * 
 * @author BLI
 */
public class SerialDataInterpreter {
    
    private SerialDataFilter serial;

    float deltaX = 0;
    float deltaY = 0;
    private float outputYawRadians,outputPitchRadians,outputRollRadians;
    
    private boolean recording = false;
    private ProbeDataWriter currentXYRecording;
    private ProbeDataWriter currentYawPitchRollRecording;


    public SerialDataInterpreter(Properties props) {
        serial = new SerialDataFilter(props);
        System.out.println("Waiting to receive input...");
    }
    
    public void startStopRecording(Path filePath){
        try {
            
            if(recording){
                currentXYRecording.closeWriter();
                currentYawPitchRollRecording.closeWriter();
                currentXYRecording = null;
                currentYawPitchRollRecording = null;
                
                
            }else{   
                String currentTimestamp = ProbeDataHelper.getTimestampSuffix();
                currentXYRecording = new ProbeDataWriter(
                        filePath,"pathXYdata",currentTimestamp);
                currentYawPitchRollRecording = new ProbeDataWriter(
                        filePath,"pathYawPitchRollData",currentTimestamp);
                
            }
            
            recording = !recording; //flips the switch saying whether it is recording or not
        } catch (IOException ex) {
            System.out.println(ex);
        }
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
        
        if(recording){
            try {
                currentXYRecording.writeLine(deltaX + "," + deltaY);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }
    
    private void processYawPitchRoll(){
        outputYawRadians = getOutputAngle(serial.getCurrentYaw());
        outputRollRadians = getOutputAngle(serial.getCurrentRoll());
        outputPitchRadians = getOutputAngle(serial.getCurrentPitch());
        
        if(recording){
            try {
                currentYawPitchRollRecording.writeLine(outputYawRadians + "," + outputPitchRadians + "," + outputRollRadians);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
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
