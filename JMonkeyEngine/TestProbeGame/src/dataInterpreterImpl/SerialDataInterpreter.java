/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataInterpreterImpl;

import dataReaderImpl.SerialReader;
import java.util.HashMap;
import java.util.Properties;
import util.PropertiesHelper;

/**
 * This class is meant to instantiate the SerialReader class
 *      and then keep track of the current yaw,pitch,roll
 *      and x,y that it is processing. 
 * It should do any interpretation of x,y, and yaw,pitch,roll,
 *      just report them
 *
 * @author BLI
 */
public class SerialDataInterpreter {
    
    private Properties trackerProps;
    private static HashMap<String,Integer> dataLocations;
    private SerialReader serial;
    private boolean updateExists;
    private String currentSerialOutput;
    private String previousSerialOutput;
    private boolean showOutput;
    
    public SerialDataInterpreter(){
        
        trackerProps = PropertiesHelper.getProperties();
        
        makeDataLocationsMap();
        try{
            serial = new SerialReader();
            serial.beginExecution();
        }catch(Throwable t){
            System.out.println("CANNOT USE SERIAL DEVICE. INSTALL RXTX.");
            System.out.println(t);
        }
        
        System.out.println("Waiting to receive input...");
    }
    
    public void readSerialData(){
        updateExists = false;
        try{
            
            currentSerialOutput = serial.getCurrentOutput();
            if(!String.valueOf(currentSerialOutput).equals("null") &&
                    !currentSerialOutput.equals(previousSerialOutput)){
                
                if(showOutput){
                    System.out.println(currentSerialOutput);
                }
                updateExists = true;
                
            }
            
        }catch(Throwable e){
            System.out.println("READING SERIAL DATA FAILED!: " + e);
        }
    }
    
    private void makeDataLocationsMap(){
        
            int timestampLoc = Integer.parseInt(trackerProps.
                    getProperty("dataLocation.timestamp"));
            int xLoc = Integer.parseInt(trackerProps.
                    getProperty("dataLocation.x"));
            int yLoc = Integer.parseInt(trackerProps.
                    getProperty("dataLocation.y"));
            int yawLoc = Integer.parseInt(trackerProps.
                    getProperty("dataLocation.yaw"));
            int pitchLoc = Integer.parseInt(trackerProps.
                    getProperty("dataLocation.pitch"));
            int rollLoc = Integer.parseInt(trackerProps.
                    getProperty("dataLocation.roll"));
        
            dataLocations = new HashMap<String,Integer>(10);
            dataLocations.put("timestamp", timestampLoc);
            dataLocations.put("x", xLoc);
            dataLocations.put("y", yLoc);
            dataLocations.put("yaw", yawLoc);
            dataLocations.put("pitch", pitchLoc);
            dataLocations.put("roll", rollLoc);
        }
    
}
