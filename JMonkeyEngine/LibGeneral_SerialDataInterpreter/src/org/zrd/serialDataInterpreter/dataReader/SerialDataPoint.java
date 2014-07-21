/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataReader;

import java.util.HashMap;
import org.zrd.util.debugOutput.OutputHelper;

/**
 * This class represents a single point in the serial data
 *      and is responsible for converting the string
 *      to x,y and yaw,pitch,roll floats as well as numbers
 *      for any other data that the probe is giving
 * 
 * This class takes a string as well as a hash map
 *      for the data location numbers
 *
 * @author BLI
 */
public class SerialDataPoint {
    //says the delimiter used to separate parts of the serial data string
    public static final String SERIAL_DATA_STRING_DELIMITER = ",";
    
    /**
     *Min number of data parts before it parses the string.
     *      This prevents small strings from the serial reading
     *      being parsed and causing errors
     */
    public static final int MIN_NUM_DATA_PARTS = 6;
    
    //default field value if not specified by data string
    public static final float DEFAULT_FIELD_VALUE = Float.NaN;
    
    private float timestamp,x,y,yaw,pitch,roll,datafield;
    private String[] dataParts;
    private HashMap<String,Integer> dataLocations;
    
    /**
     * Constructs the data point from the string and map
     * @param data              the data string
     * @param dataLocations     data locations map
     */
    public SerialDataPoint(String data, HashMap<String,Integer> dataLocations){
        this.dataLocations = dataLocations;
        
        dataParts = data.split(SERIAL_DATA_STRING_DELIMITER);
        
        //make sure we currently have enough data
        if(dataParts.length >= MIN_NUM_DATA_PARTS){
            x = getPart(DataLocationConstants.X_KEY);
            y = getPart(DataLocationConstants.Y_KEY);
            yaw = getPart(DataLocationConstants.YAW_KEY);
            pitch = getPart(DataLocationConstants.PITCH_KEY);
            roll = getPart(DataLocationConstants.ROLL_KEY);
            timestamp = getPart(DataLocationConstants.TIMESTAMP_KEY);
            datafield = getPart(DataLocationConstants.DATAFIELD_KEY);
        }
    }
    
    private float getPart(String partName){
        if(dataLocations.containsKey(partName)){
            return Float.parseFloat(dataParts[dataLocations.get(partName)]);
        }else{
            return DEFAULT_FIELD_VALUE;
        }
    }

    @Override
    public String toString() {
        return OutputHelper.makeNameValueDisplay(DataLocationConstants.TIMESTAMP_KEY,timestamp) +
                OutputHelper.makeNameValueDisplay(DataLocationConstants.YAW_KEY,yaw) +
                OutputHelper.makeNameValueDisplay(DataLocationConstants.PITCH_KEY,pitch) +
                OutputHelper.makeNameValueDisplay(DataLocationConstants.ROLL_KEY,roll) +
                OutputHelper.makeNameValueDisplay(DataLocationConstants.X_KEY,x) +
                OutputHelper.makeNameValueDisplay(DataLocationConstants.Y_KEY,y) +
                OutputHelper.makeNameValueDisplay(DataLocationConstants.DATAFIELD_KEY,datafield);
    }
    
    
    
    public float getTimestamp() {
        return timestamp;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public float getRoll() {
        return roll;
    }

    public float getY() {
        return y;
    }
    public float getX() {
        return x;
    }
    
}
