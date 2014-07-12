/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataReader;

import org.zrd.serialInterface.arduinoReading.SerialReader;
import java.util.HashMap;
import java.util.Properties;

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
public class SerialDataReader {
    
    private SerialReader serial;
    private SerialDataPoint currentSerialData;

    private String currentSerialOutput, previousSerialOutput;
    
    private float deltaX,deltaY,currentYaw,currentPitch,currentRoll;
    
    //flag for only showing output and not processing it
    private boolean onlyDoOutput;
    
    //flag for whether or not to parse the output. If not parsed, raw string is shown
    private boolean parseOutput;
    
    private boolean updateExists = false;
    private boolean showOutput;
    
    private static HashMap<String,Integer> dataLocations;
    private Properties trackerProps;

    public SerialDataReader(Properties trackerProps) {
        this.trackerProps = trackerProps;
        
        dataLocations = DataLocationsMap.getDataLocationMap(trackerProps);
        try{
            serial = new SerialReader();
            serial.beginExecution(trackerProps);
        }catch(Throwable t){
            System.out.println("CANNOT USE SERIAL DEVICE. INSTALL RXTX.");
            System.out.println(t);
        }
        
        System.out.println("Waiting to receive input...");
        
        onlyDoOutput = Boolean.parseBoolean(
                trackerProps.getProperty(
                "arduinoData.onlyDoOutput"));
        
        parseOutput = Boolean.parseBoolean(
                trackerProps.getProperty(
                "arduinoData.parseOutput"));
        
        showOutput = Boolean.parseBoolean(
                trackerProps.getProperty(
                "arduinoData.showOutput"));
        
        
        
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

    public void updateData(){
        
        readSerialData();

        if(!onlyDoOutput){
            processArdData();
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
