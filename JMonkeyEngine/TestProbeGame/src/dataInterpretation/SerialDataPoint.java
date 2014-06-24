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
    
    /*private float yaw;
    private float pitch;
    private float roll;
    
    private float x;
    private float y;*/
    
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

    /*@Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Float.floatToIntBits(this.timestamp);
        hash = 79 * hash + Float.floatToIntBits(this.yaw);
        hash = 79 * hash + Float.floatToIntBits(this.pitch);
        hash = 79 * hash + Float.floatToIntBits(this.roll);
        hash = 79 * hash + Float.floatToIntBits(this.x);
        hash = 79 * hash + Float.floatToIntBits(this.y);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SerialDataPoint other = (SerialDataPoint) obj;
        if (Float.floatToIntBits(this.timestamp) != Float.floatToIntBits(other.timestamp)) {
            return false;
        }
        if (Float.floatToIntBits(this.yaw) != Float.floatToIntBits(other.yaw)) {
            return false;
        }
        if (Float.floatToIntBits(this.pitch) != Float.floatToIntBits(other.pitch)) {
            return false;
        }
        if (Float.floatToIntBits(this.roll) != Float.floatToIntBits(other.roll)) {
            return false;
        }
        if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x)) {
            return false;
        }
        if (Float.floatToIntBits(this.y) != Float.floatToIntBits(other.y)) {
            return false;
        }
        return true;
    }*/
    
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
