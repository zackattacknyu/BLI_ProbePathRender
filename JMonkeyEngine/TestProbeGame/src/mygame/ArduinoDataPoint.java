/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

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
    
    private String[] dataParts;
    private HashMap<String,Integer> dataLocations;
    
    public ArduinoDataPoint(String data, HashMap<String,Integer> dataLocations){
        this.dataLocations = dataLocations;
        dataParts = data.split(",");
        yaw = getPart("yaw");
        pitch = getPart("pitch");
        roll = getPart("roll");
        timestamp = getPart("timestamp");
    }
    
    private float getPart(String partName){
        if(dataLocations.containsKey(partName)){
            return Float.parseFloat(dataParts[dataLocations.get(partName)]);
        }else{
            return 0;
        }
    }
    
    public ArduinoDataPoint(String data, String format){
        
        String[] dataParts = data.split(",");
        String[] formatParts = format.split(",");
        String formatPart;
        float currentNumber = 0;
        
        for(int index = 0; index < formatParts.length; index++){
            formatPart = formatParts[index].toLowerCase();
            
            if(!formatPart.equals("null")){
                currentNumber = Float.parseFloat(dataParts[index]);
            }
            
            if(formatPart.equals("yaw")){
                yaw = currentNumber;
            }else if(formatPart.equals("pitch")){
                pitch = currentNumber;
            }else if(formatPart.equals("roll")){
                roll = currentNumber;
            }else if(formatPart.equals("x")){
                x = currentNumber;
            }else if(formatPart.equals("y")){
                y = currentNumber;
            }else if(formatPart.equals("timestamp")){
                timestamp = currentNumber;
            }
            
            
        }
        
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
    
    
}
