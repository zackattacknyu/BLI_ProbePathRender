/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataInterpretation;

import java.nio.file.Path;
import java.util.Properties;
import org.zrd.serialDataInterpreter.dataReader.SerialDataReader;
import org.zrd.util.dataHelp.BasicAngleHelper;
import org.zrd.util.dataStreaming.ProbeDataStream;

/**
 * 
 * @author BLI
 */
public class SerialDataInterpreter implements ProbeDataStream{
    
    
    
    private SerialDataReader serial;
    private Path dataRecordingFilePath;

    float deltaX = 0;
    float deltaY = 0;
    private float outputYawRadians,outputPitchRadians,outputRollRadians;
    
    private boolean recording = false;
    private SerialDataRecorder currentDataRecorder;


    public SerialDataInterpreter(Properties props) {
        serial = new SerialDataReader(props);
        System.out.println("Waiting to receive input...");
    }
    
    public SerialDataInterpreter(Properties props, Path filePath){
        this(props);
        dataRecordingFilePath = filePath;
    }
    
    public SerialDataReader getReader(){
        return serial;
    }
    
    @Override
    public void startStopRecording(){
        if(recording){
            currentDataRecorder.closeRecording();
            recording = false;
                
        }else{   
            currentDataRecorder = new SerialDataRecorder(dataRecordingFilePath);
            recording = true;

        }
    }

    /**
     * This is meant to be called many times in a second
     *      and it updates the data from the serial probe
     */
    @Override
    public void updateData(){
        
        //this calls the reader's update data method
        serial.updateData();
        
        /*gets the output yaw,pitch, and roll
         *      The serial data is in degrees
         *          but we need radians
         *          so the method called does that conversion
         */
        outputYawRadians = BasicAngleHelper.convertDegreesToRadians(serial.getCurrentYaw());
        outputRollRadians = BasicAngleHelper.convertDegreesToRadians(serial.getCurrentRoll());
        outputPitchRadians = BasicAngleHelper.convertDegreesToRadians(serial.getCurrentPitch());
        
        /*
         * This gets the output x and y.
         *  For now, it is just the x and y values 
         *      direct from the probe but if in the future
         *      it should change, then this code should call
         *      a method for that.
         */
        deltaX = serial.getDeltaX();
        deltaY = serial.getDeltaY();

        if(recording){
            currentDataRecorder.addLineToFiles(deltaX, deltaY, 
                    outputYawRadians, 
                    outputPitchRadians, 
                    outputRollRadians);
        }
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
   
}
