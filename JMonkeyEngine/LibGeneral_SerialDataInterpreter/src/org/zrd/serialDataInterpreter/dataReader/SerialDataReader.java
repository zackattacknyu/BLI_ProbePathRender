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
import org.zrd.util.dataStreaming.StreamQualityTracker;

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
public class SerialDataReader implements ProbeDataStream,StreamQualityTracker{

    private SerialReader serial;
    
    private SerialDataPoint currentSerialData;
    private String currentSerialOutput, previousSerialOutput;
    private float deltaX,deltaY,currentYaw,currentPitch,currentRoll,currentQuality;
    
    private float deltaXlow,deltaXhigh,deltaYlow,deltaYhigh;
    
    /**flag for whether or not to parse the output. If not parsed, raw string is shown*/
    private boolean parseOutput;
    
    /**flag for whether or not to show each line of the output to the user*/
    private boolean showOutput;
    
    /**the data locations hashmap used to go from string to data*/
    private HashMap<String,Integer> dataLocations;
    
    //variables used for recording the raw data
    private boolean recordingRawData = false;
    private RawSerialRecorder currentRecorder;
    private Path recordingFilePath;
    private String[] currentDataString;

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
    
    public SerialReader getSerialInterface(){
        return serial;
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
        
        try{
            
            //peeks into the reader
            currentSerialOutput = serial.getCurrentOutput();
            
            //seeks if the string is non-null and different from the last string
            if(!String.valueOf(currentSerialOutput).equals("null")
                    && !currentSerialOutput.equals(previousSerialOutput)){
                
                //parses the string if that is specified
                if(parseOutput){
                    
                    currentSerialData = new SerialDataPoint(currentSerialOutput,dataLocations);
                    
                    //deltaX = currentSerialData.getX();
                    //deltaY = currentSerialData.getY();
                    
                    deltaXlow = currentSerialData.getXlow();
                    deltaXhigh = currentSerialData.getXhigh();
                    deltaYlow = currentSerialData.getYlow();
                    deltaYhigh = currentSerialData.getYhigh();

                    currentPitch = currentSerialData.getPitch();
                    currentRoll = currentSerialData.getRoll();
                    currentYaw = currentSerialData.getYaw();
                    
                    currentQuality = currentSerialData.getQuality();
                    
                    currentDataString = currentSerialData.getDataAtPoint();
                    
                    //prints the parsed output if desired
                    if(showOutput) System.out.println(currentSerialData);
                    
                }else if(showOutput){
                    
                    //if no parsing but printing of output is desired, then print raw output
                    System.out.println(currentSerialOutput);
                    
                }
                
                //record the raw data if desired
                if(recordingRawData){
                    currentRecorder.addLineToFiles(currentSerialOutput);
                }
                
                previousSerialOutput = currentSerialOutput;
            }
            
        }catch(Throwable e){
            
            //if anything failed during the reading, this gets called
            System.out.println("READING SERIAL DATA FAILED!: " + e);
        }
    }

    public String[] getCurrentDataString() {
        return currentDataString;
    }
    
    /**
     * This starts and stops the recording of the raw data
     */
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

    public float getDeltaXlow() {
        return deltaXlow;
    }

    public float getDeltaXhigh() {
        return deltaXhigh;
    }

    public float getDeltaYlow() {
        return deltaYlow;
    }

    public float getDeltaYhigh() {
        return deltaYhigh;
    }
    
    /**
     * This returns the current serial data from the probe
     * @return  the SerialDataPoint object for the current data from the probe
     */
    public SerialDataPoint getCurrentSerialData() {
        return currentSerialData;
    }

    /**
     * This gets the current raw x displacement from the probe
     * @return  raw x displacement
     */
    public float getDeltaX() {
        return deltaX;
    }

    /**
     * This gets the current raw y displacement the probe
     * @return   raw y displacement
     */
    public float getDeltaY() {
        return deltaY;
    }

    /**
     * This gets the current yaw reading from the probe
     * @return      current yaw on the probe
     */
    public float getCurrentYaw() {
        return currentYaw;
    }

    /**
     * This gets the current pitch from the probe
     * @return      current pitch on the probe
     */
    public float getCurrentPitch() {
        return currentPitch;
    }

    /**
     * This gets the current roll from the probe
     * @return      current roll on the probe
     */
    public float getCurrentRoll() {
        return currentRoll;
    }

    @Override
    public float getCurrentQuality() {
        return currentQuality;
    }

    
    
    
   
}
