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
 * This class takes in a reader which gives it raw orientation and 
 *      displacement readings and decides what to tell the tracker
 *      which is keeping track of the probe's position
 *      and orientation.
 * 
 * It also starts, stops, and passes information to the recording
 *      method which records that data into text files when desired. 
 * 
 * The x,y displacement is just passed to the next level
 * 
 * The yaw,pitch,roll data gets converted from degrees to radians
 *      before being passed to the next level
 * 
 * @author BLI
 */
public class SerialDataInterpreter implements ProbeDataStream{
    
    
    
    private SerialDataReader serial;
    private Path dataRecordingFilePath;

    private float deltaX = 0;
    private float deltaY = 0;
    private float outputYawRadians,outputPitchRadians,outputRollRadians;
    
    private boolean recording = false;
    private SerialDataRecorder currentDataRecorder;

    /**
     * Initializes the serial interpreter by initializing the serial reader 
     *      which is at a layer below it
     * @param props         The properties used to initialize the reader
     */
    public SerialDataInterpreter(Properties props) {
        serial = new SerialDataReader(props);
        System.out.println("Waiting to receive input...");
    }
    
    /**
     * Initializes the serial reader by initializing the serial reader which is
     *      a layer below it and also takes the the file path in the event
     *      that the data will want to be recorded
     * @param props     properties used to initialize reader
     * @param filePath  folder path to put text files of probe data
     */
    public SerialDataInterpreter(Properties props, Path filePath){
        this(props);
        dataRecordingFilePath = filePath;
    }
    
    /**
     * This is meant to be called many times in a second
     *      and it takes the current data from the serial probe
     *      and calls the methods that give us the current
     *      x,y and yaw,pitch,roll
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

        /*
         * if we are recording the data, this adds the current data
         *      to the files being generated
         */
        if(recording){
            currentDataRecorder.addLineToFiles(deltaX, deltaY, 
                    outputYawRadians, 
                    outputPitchRadians, 
                    outputRollRadians);
        }
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
   
    public SerialDataReader getReader(){
        return serial;
    }
}
