/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataReader;

import java.util.HashMap;

/**
 *
 * @author BLI
 */
public class SerialDataPoint {
    
    private float timestamp;
    
    private ProbeDataPoint dataPoint;
    private String[] dataParts;
    private HashMap<String,Integer> dataLocations;
    
    public SerialDataPoint(String data, HashMap<String,Integer> dataLocations){
        this.dataLocations = dataLocations;
        extractData(data);
    }
    
    private void extractData(String data){
        dataParts = data.split(DataLocationConstants.SERIAL_DATA_STRING_DELIMITER);
        float x,y,yaw,pitch,roll;
        x = getPart(DataLocationConstants.X_KEY);
        y = getPart(DataLocationConstants.Y_KEY);
        yaw = getPart(DataLocationConstants.YAW_KEY);
        pitch = getPart(DataLocationConstants.PITCH_KEY);
        roll = getPart(DataLocationConstants.ROLL_KEY);
        dataPoint = new ProbeDataPoint(yaw,pitch,roll,x,y);
        timestamp = getPart(DataLocationConstants.TIMESTAMP_KEY);
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
                + ", yaw=" + dataPoint.getYaw()
                + ", pitch=" + dataPoint.getPitch()
                + ", roll=" + dataPoint.getRoll()
                + ", x=" + dataPoint.getX()
                + ", y=" + dataPoint.getY();
    }
    
    public float getTimestamp() {
        return timestamp;
    }

    public float getYaw() {
        return dataPoint.getYaw();
    }

    public float getPitch() {
        return dataPoint.getPitch();
    }

    public float getRoll() {
        return dataPoint.getRoll();
    }

    public float getY() {
        return dataPoint.getY();
    }
    public float getX() {
        return dataPoint.getX();
    }
    
}
