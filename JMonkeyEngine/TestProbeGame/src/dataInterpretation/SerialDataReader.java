/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataInterpretation;

import dataReaderImpl.SerialReader;
import util.PropertiesHelper;
import java.util.HashMap;
import java.util.Properties;

/**
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

    public SerialDataReader() {
        trackerProps = PropertiesHelper.getProperties();
        
        dataLocations = DataLocationsMap.getDataLocationMap(trackerProps);
        try{
            serial = new SerialReader();
            serial.beginExecution();
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

        deltaX = deltaX/1000.0f;
        deltaY = deltaY/1000.0f;
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
