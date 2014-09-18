/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataReader;

import java.util.Arrays;
import java.util.HashMap;
import org.zrd.util.dataHelp.OutputHelper;

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
    
    //default field value if not specified by data string
    public static final float DEFAULT_FIELD_VALUE = Float.NaN;
    
    private float timestamp,x,y,yaw,pitch,roll,datafield,quality;
    private float xlow,xhigh,ylow,yhigh;
    private String[] dataParts;
    private HashMap<String,Integer> dataLocations;
    
    private String[] dataAtPoint;
    private int dataIndexStart = 8;
    private int dataIndexEnd = 207;
    
    /**
     * Constructs the data point from the string and map
     * @param data              the data string
     * @param dataLocations     data locations map
     */
    public SerialDataPoint(String data, HashMap<String,Integer> dataLocations){
        this.dataLocations = dataLocations;
        
        dataParts = data.split(SERIAL_DATA_STRING_DELIMITER);
        
        //make sure we currently have enough data
        if(dataParts.length >= dataLocations.size()){
            //x = getPart(DataLocationsMap.X_KEY);
            //y = getPart(DataLocationsMap.Y_KEY);
            yaw = getPart(DataLocationsMap.YAW_KEY);
            pitch = getPart(DataLocationsMap.PITCH_KEY);
            roll = getPart(DataLocationsMap.ROLL_KEY);
            timestamp = getPart(DataLocationsMap.TIMESTAMP_KEY);
            datafield = getPart(DataLocationsMap.DATAFIELD_KEY);
            quality = getPart(DataLocationsMap.QUALITY_KEY);
            
            xlow = getPart(DataLocationsMap.X_LOW_KEY);
            xhigh = getPart(DataLocationsMap.X_HIGH_KEY);
            ylow = getPart(DataLocationsMap.Y_LOW_KEY);
            yhigh = getPart(DataLocationsMap.Y_HIGH_KEY);
        }
        
        dataAtPoint = Arrays.copyOfRange(dataParts, dataIndexStart, dataIndexEnd+1);
    }

    public String[] getDataAtPoint() {
        return dataAtPoint;
    }
    
    /**
     * This gets the value of the field described by the name. 
     *      It retrieves the index from the data location map
     *          and gets the value at the index in the array
     *          of data values obtained from parsing the string
     * 
     * @param partName      the name of the field
     * @return              value of the field
     */
    private float getPart(String partName){
        if(dataLocations.containsKey(partName)){
            return Float.parseFloat(dataParts[dataLocations.get(partName)]);
        }else{
            return DEFAULT_FIELD_VALUE;
        }
    }

    /**
     * This converts the data point to a string for the console output
     * 
     * @return      string showing all the data at this point
     */
    @Override
    public String toString() {
        /*return OutputHelper.makeNameValueDisplay(DataLocationsMap.TIMESTAMP_KEY,timestamp) +
                OutputHelper.makeNameValueDisplay(DataLocationsMap.YAW_KEY,yaw) +
                OutputHelper.makeNameValueDisplay(DataLocationsMap.PITCH_KEY,pitch) +
                OutputHelper.makeNameValueDisplay(DataLocationsMap.ROLL_KEY,roll) +
                OutputHelper.makeNameValueDisplay(DataLocationsMap.X_KEY,x) +
                OutputHelper.makeNameValueDisplay(DataLocationsMap.Y_KEY,y) +
                OutputHelper.makeNameValueDisplay(DataLocationsMap.DATAFIELD_KEY,datafield) +
                OutputHelper.makeNameValueDisplay(DataLocationsMap.QUALITY_KEY, quality) + 
                "totalData: " + dataAtPoint;*/
        return OutputHelper.makeNameValueDisplay(DataLocationsMap.TIMESTAMP_KEY,timestamp) + 
                " " + dataAtPoint;
    }

    public float getXlow() {
        return xlow;
    }

    public float getXhigh() {
        return xhigh;
    }

    public float getYlow() {
        return ylow;
    }

    public float getYhigh() {
        return yhigh;
    }
    
    /**
     * The timestamp at this particular data point
     * @return      timestamp in the string
     */    
    public float getTimestamp() {
        return timestamp;
    }

    /**
     * The yaw at this particular data point
     * @return      yaw in the string
     */  
    public float getYaw() {
        return yaw;
    }

    /**
     * The pitch at this particular data point
     * @return      pitch in the string
     */  
    public float getPitch() {
        return pitch;
    }

    /**
     * The roll at this particular data point
     * @return      roll in the string
     */  
    public float getRoll() {
        return roll;
    }

    /**
     * The y at this particular data point
     * @return      y in the string
     */  
    public float getY() {
        return y;
    }
    
    /**
     * The x at this particular data point
     * @return      x in the string
     */  
    public float getX() {
        return x;
    }

    public float getQuality() {
        return quality;
    }
    
}
