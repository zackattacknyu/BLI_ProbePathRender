/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataInterpretation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;
import org.zrd.util.dataWriting.ProbeDataWriter;
import org.zrd.serialDataInterpreter.dataFilter.SerialDataFilter;
import org.zrd.util.timeTools.TimeHelper;

/**
 * 
 * @author BLI
 */
public class SerialDataInterpreter {
    
    public static final float DEG_TO_RAD_FACTOR = (float) (Math.PI/180.0);
    
    private SerialDataFilter serial;

    float deltaX = 0;
    float deltaY = 0;
    private float outputYawRadians,outputPitchRadians,outputRollRadians;
    
    private boolean recording = false;
    private ProbeDataWriter currentXYRecording;
    private ProbeDataWriter currentYawPitchRollRecording,currentPathOutputWriter;


    public SerialDataInterpreter(Properties props) {
        serial = new SerialDataFilter(props);
        System.out.println("Waiting to receive input...");
    }
    
    public void startStopRecording(Path filePath){
        try {
            
            if(recording){
                currentPathOutputWriter.closeWriter();
                currentXYRecording.closeWriter();
                currentYawPitchRollRecording.closeWriter();
                currentXYRecording = null;
                currentYawPitchRollRecording = null;
                currentPathOutputWriter = null;
                recording = false;
                
            }else{   
                String currentTimestamp = TimeHelper.getTimestampSuffix();
                currentXYRecording = new ProbeDataWriter(
                        filePath,"pathXYdata",currentTimestamp);
                currentYawPitchRollRecording = new ProbeDataWriter(
                        filePath,"pathYawPitchRollData",currentTimestamp);
                currentPathOutputWriter = new ProbeDataWriter(
                        filePath,"pathOutput",currentTimestamp);
                recording = true;
                
            }
            
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void updateData(){
        serial.updateData();
        processYawPitchRoll();
        processXYdata();
        try {
            if(recording){
                currentPathOutputWriter.writeLine(outputYawRadians + "," + 
                    outputRollRadians + "," + outputPitchRadians +
                    "," + deltaX + "," + deltaY );
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
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
     * 
     * In this case, it takes the input which is in degrees and converts
     *      it to radians
     * @param angle
     * @return 
     */
    private float getOutputAngle(float angle){
        return angle*DEG_TO_RAD_FACTOR;
    }
    
    /**
     * This is the method that takes in an x or y value from the probe
     *      and returns the desired displacement to the tracker.
     * 
     * 
     * @param disp
     * @return 
     */
    private float getOutputDisp(float disp){
        return disp;
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
