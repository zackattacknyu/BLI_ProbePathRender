/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataInterpretation;

import java.nio.file.Path;
import java.util.Properties;
import org.zrd.serialDataInterpreter.dataReader.SerialDataReader;

/**
 * 
 * @author BLI
 */
public class SerialDataInterpreter {
    
    public static final float DEG_TO_RAD_FACTOR = (float) (Math.PI/180.0);
    
    private SerialDataReader serial;

    float deltaX = 0;
    float deltaY = 0;
    private float outputYawRadians,outputPitchRadians,outputRollRadians;
    
    private boolean recording = false;
    private SerialDataRecorder currentDataRecorder;


    public SerialDataInterpreter(Properties props) {
        serial = new SerialDataReader(props);
        System.out.println("Waiting to receive input...");
    }
    
    public void startStopRecording(Path filePath){
        if(recording){
            currentDataRecorder.closeRecording();
            recording = false;
                
        }else{   
            currentDataRecorder = new SerialDataRecorder(filePath);
            recording = true;

        }
    }

    public void updateData(){
        serial.updateData();
        processYawPitchRoll();
        processXYdata();
        if(recording){
            currentDataRecorder.addLineToFiles(deltaX, deltaY, 
                    outputYawRadians, 
                    outputPitchRadians, 
                    outputRollRadians);
        }
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

    public float getOutputYawRadians() {
        return outputYawRadians;
    }

    public float getOutputPitchRadians() {
        return outputPitchRadians;
    }

    public float getOutputRollRadians() {
        return outputRollRadians;
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
