/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataReader;

import java.util.HashMap;

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
    
    private float timestamp,x,y,yaw,pitch,roll;

    public static final int MIN_NUM_DATA_PARTS = 6;
    
    private String[] dataParts;
    private HashMap<String,Integer> dataLocations;
    
    /**
     * Constructs the data point from the string and map
     * @param data              the data string
     * @param dataLocations     data locations map
     */
    public SerialDataPoint(String data, HashMap<String,Integer> dataLocations){
        this.dataLocations = dataLocations;
        
        dataParts = data.split(DataLocationConstants.SERIAL_DATA_STRING_DELIMITER);
        
        //make sure we currently have enough data
        if(dataParts.length >= MIN_NUM_DATA_PARTS){
            x = getPart(DataLocationConstants.X_KEY);
            y = getPart(DataLocationConstants.Y_KEY);
            yaw = getPart(DataLocationConstants.YAW_KEY);
            pitch = getPart(DataLocationConstants.PITCH_KEY);
            roll = getPart(DataLocationConstants.ROLL_KEY);
            timestamp = getPart(DataLocationConstants.TIMESTAMP_KEY);
        }
    }
    
    private float getPart(String partName){
        if(dataLocations.containsKey(partName)){
            return Float.parseFloat(dataParts[dataLocations.get(partName)]);
        }else{
            return 0;
        }
    }

    @Override
    public String toString() {
        return "timestamp=" + timestamp 
                + ", yaw=" + yaw
                + ", pitch=" + pitch
                + ", roll=" + roll
                + ", x=" + x
                + ", y=" + y;
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
