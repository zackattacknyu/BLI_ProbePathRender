/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataInterpretation;

import com.jme3.math.Quaternion;
import java.util.HashMap;

/**
 *
 * @author BLI
 */
public class ArduinoDataPoint {
    
    private float timestamp;
    
    private float yaw;
    private float pitch;
    private float roll;

    

    private float x;
    private float y;
    
    private Quaternion rotation;
    
    private String[] dataParts;
    private HashMap<String,Integer> dataLocations;
    
    public ArduinoDataPoint(String data, HashMap<String,Integer> dataLocations){
        this.dataLocations = dataLocations;
        dataParts = data.split(",");
        x = getPart("x");
        y = getPart("y");
        yaw = getPart("yaw");
        pitch = getPart("pitch");
        roll = getPart("roll");
        timestamp = getPart("timestamp");
        rotation = new Quaternion(yaw,pitch,roll,1);
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

    @Override
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
        final ArduinoDataPoint other = (ArduinoDataPoint) obj;
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
    }
    
    
    
    public Quaternion getRotation() {
        return rotation;
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
