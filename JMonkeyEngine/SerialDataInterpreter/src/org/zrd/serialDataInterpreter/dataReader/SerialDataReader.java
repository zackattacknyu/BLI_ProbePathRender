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
 * This class does only the following things:
 *      - Makes sure the current string from the probe is different from the last one
 *      - Calls the code that parses the string and returns the result
 *      - Calls the code that records the raw string
 * 
 * TODO: Make layer diagram for probe data interpretation
 *      classes
 * 
 * 
 * 
 * @author BLI
 */
public class SerialDataReader implements ProbeDataStream{
    
    //the raw reader
    private SerialReader serial;
    
    private SerialDataPoint currentSerialData;
    private String currentSerialOutput, previousSerialOutput;
    private float deltaX,deltaY,currentYaw,currentPitch,currentRoll;
    
    //flag for whether or not to parse the output. If not parsed, raw string is shown
    private boolean parseOutput;
    
    //flag for whether or not to show each line of the output to the user
    private boolean showOutput;
    
    //internal variable used to say whether a new reading has been found
    private boolean updateExists = false;
    
    //the data locations hashmap used to go from string to data
    private HashMap<String,Integer> dataLocations;
    
    //variables used for recording the raw data
    private boolean recordingRawData = false;
    private RawSerialRecorder currentRecorder;
    private Path recordingFilePath;

    /**
     * This initializes the serial data reader. It is a private constructor
     *      because it is used by the public constructors for the two domains
     *      that this class is currently being used for
     * @param trackerProps      properties that say data locations as well as serial reading properties
     * @param parseOutput       whether or not to parse the output
     */
    private SerialDataReader(Properties trackerProps, boolean parseOutput) {
        this.parseOutput = parseOutput;
        
        dataLocations = DataLocationsMap.getDataLocationMap(trackerProps);
        serial = SerialReader.startNewReader(trackerProps);
        
        System.out.println("Waiting to receive input...");
        
        showOutput = Boolean.parseBoolean(
                trackerProps.getProperty(
                "arduinoData.showOutput"));
    }
    
    /**
     * This initializes the reader and is designed to be used by a program
     *      that will record the raw data from the reader. 
     * Recording of the parsed data is NOT supported here and should be done
     *      at a class that is at a higher level, thus this only supports
     *      raw data recording
     * @param trackerProps          properties that say data locations as well as serial reading properties
     * @param recordingFilePath     path to put the text files with probe data
     */
    public SerialDataReader(Properties trackerProps,Path recordingFilePath){
        this(trackerProps,false);
        this.recordingFilePath = recordingFilePath;
    }
    
    /**
     * This initializes the reader and is designed to be used by a class that 
     *      is at a higher level and will interpret the parsed data.
     * Recording of any data is NOT supported if we do this because any program
     *      using this as one of the levels should be doing its recording at 
     *      one of the higher levels.
     * @param trackerProps      properties that say data locations as well as serial reading properties
     */
    public SerialDataReader(Properties trackerProps){
        this(trackerProps,true);
    }
    
    /**
     * This peeks into the serial output and only updates the current values
     *      if a new one has emerged
     */
    @Override
    public void updateData(){
        //starts by assuming there is no new update
        updateExists = false;
        
        try{
            
            //peeks into the reader
            currentSerialOutput = serial.getCurrentOutput();
            
            //seeks if the string is non-null and different from the last string
            if(!String.valueOf(currentSerialOutput).equals("null")
                    && !currentSerialOutput.equals(previousSerialOutput)){
                
                //parses the string if that is specified
                if(parseOutput){
                    currentSerialData = new SerialDataPoint(currentSerialOutput,dataLocations);
                    processObjectUpdate();
                }
                
                //shows the current output
                if(showOutput){
                    
                    if(parseOutput){
                        
                        //if parsing, show the fields and their values
                        System.out.println(currentSerialData);
                    }else{
                        
                        //if no parsing, just show raw output
                        System.out.println(currentSerialOutput);
                        
                    }
                    
                }
                
                //record the raw data if desired
                if(recordingRawData){
                    currentRecorder.addLineToFiles(currentSerialOutput);
                }
                
                //informs other things that an update has indeed occured
                updateExists = true;
                
                previousSerialOutput = currentSerialOutput;
            }
            
        }catch(Throwable e){
            
            //if anything failed during the reading, this gets called
            System.out.println("READING SERIAL DATA FAILED!: " + e);
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
