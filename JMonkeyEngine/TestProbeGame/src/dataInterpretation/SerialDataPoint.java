/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataInterpretation;

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
        dataParts = data.split(",");
        float x,y,yaw,pitch,roll;
        x = getPart("x");
        y = getPart("y");
        yaw = getPart("yaw");
        pitch = getPart("pitch");
        roll = getPart("roll");
        dataPoint = new ProbeDataPoint(yaw,pitch,roll,x,y);
        timestamp = getPart("timestamp");
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
