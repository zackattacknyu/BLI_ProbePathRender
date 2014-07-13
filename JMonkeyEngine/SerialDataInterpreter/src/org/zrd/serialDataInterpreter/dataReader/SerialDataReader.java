/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataReader;

import java.nio.file.Path;
import org.zrd.serialInterface.arduinoReading.SerialReader;
import java.util.HashMap;
import java.util.Properties;
import org.zrd.util.dataStreaming.ProbeDataStream;

/**This class is meant to be used to initialize and run the serial reader and
 *      then parse the string that it is returned. 
 * 
 * The SerialReader class initializes a thread that constantly checks
 *      the probe data. This class peeks at that thread to see the string.
 * In practice, we ended up peeking at the thread much more often than new
 *      values were being generated, so there is a check in here to make sure
 *      the current output is different from the last output.
 * 
 * This class only parses the String and any manipulations and interpretations
 *      of the values in that string should be done in later classes.
 * 
 * TODO: Make layer diagram for probe data interpretation
 *      classes
 * 
 * 
 * 
 * @author BLI
 */
public class SerialDataReader implements ProbeDataStream{
    
    private SerialReader serial;
    private SerialDataPoint currentSerialData;

    private String currentSerialOutput, previousSerialOutput;
    
    private float deltaX,deltaY,currentYaw,currentPitch,currentRoll;
    
    //flag for whether or not to parse the output. If not parsed, raw string is shown
    private boolean parseOutput;
    
    private boolean updateExists = false;
    private boolean showOutput;
    
    private static HashMap<String,Integer> dataLocations;
    
    private boolean recordingRawData = false;
    private RawSerialRecorder currentRecorder;
    private Path recordingFilePath;

    public SerialDataReader(Properties trackerProps, boolean parseOutput) {
        this.parseOutput = parseOutput;
        
        dataLocations = DataLocationsMap.getDataLocationMap(trackerProps);
        serial = SerialReader.startNewReader(trackerProps);
        
        System.out.println("Waiting to receive input...");
        
        showOutput = Boolean.parseBoolean(
                trackerProps.getProperty(
                "arduinoData.showOutput"));
    }
    
    public SerialDataReader(Properties trackerProps,Path recordingFilePath){
        this(trackerProps,false);
        this.recordingFilePath = recordingFilePath;
    }
    
    public SerialDataReader(Properties trackerProps){
        this(trackerProps,true);
    }
    
    private void readSerialData(){
        updateExists = false;
        try{
            
            currentSerialOutput = serial.getCurrentOutput();
            if(!String.valueOf(currentSerialOutput).equals("null")
                    && !currentSerialOutput.equals(previousSerialOutput)){
                
                if(parseOutput){
                    currentSerialData = new SerialDataPoint(currentSerialOutput,dataLocations);
                }
                
                //shows the current output
                if(showOutput){
                    
                    if(parseOutput){
                        //if parsing, show the result
                        System.out.println(currentSerialData);
                    }else{
                        //if no parsing, just show raw output
                        System.out.println(currentSerialOutput);
                    }
                    
                }
                
                if(recordingRawData){
                    currentRecorder.addLineToFiles(currentSerialOutput);
                }
                
                updateExists = true;
                previousSerialOutput = currentSerialOutput;
            }
            
        }catch(Throwable e){
            System.out.println("READING SERIAL DATA FAILED!: " + e);
        }
    }

    public String getCurrentSerialOutput() {
        return currentSerialOutput;
    }
    
    private void processArdData(){
        if(updateExists){
            processObjectUpdate(); 
        }
    }

    @Override
    public void updateData(){
        
        readSerialData();

        if(parseOutput){
            processArdData();
        }
    }
    
    @Override
    public void startStopRecording() {
        if(recordingRawData){
            currentRecorder.closeRecording();
            recordingRawData = false;
        }else{
            currentRecorder = new RawSerialRecorder(recordingFilePath);
            recordingRawData = true;
        }
    }
    
    private void processObjectUpdate(){
        
        processYawPitchRoll();
        processXYdata();

    }
    
    private void processXYdata(){
        deltaX = currentSerialData.getX();
        deltaY = currentSerialData.getY();
    }
    
    private void processYawPitchRoll(){
        
        currentPitch = currentSerialData.getPitch();
        currentRoll = currentSerialData.getRoll();
        currentYaw = currentSerialData.getYaw();
        
    }

    public SerialDataPoint getCurrentSerialData() {
        return currentSerialData;
    }

    public float getDeltaX() {
        return deltaX;
    }

    public float getDeltaY() {
        return deltaY;
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

    public boolean isUpdateExists() {
        return updateExists;
    }

    
    
    
   
}
