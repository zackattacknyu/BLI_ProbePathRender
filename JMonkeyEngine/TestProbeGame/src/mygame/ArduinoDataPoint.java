/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

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
