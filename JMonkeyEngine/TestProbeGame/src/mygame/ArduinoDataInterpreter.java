/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

/**
 *
 * @author BLI
 */
public class ArduinoDataInterpreter {
    
    private SerialReader serial;
    private ArduinoDataPoint currentArdData;
    private ArduinoDataPoint previousArdData;
    private float lastXangle = 0;
    private float currentXangle = 0;
    private float lastZangle = 0;
    private float currentZangle = 0;
    private float lastYangle = 0;
    private float currentYangle = 0;
    float deltaXangle=0;
    float deltaYangle=0;
    float deltaZangle=0; 
    float deltaX = 0;
    float deltaY = 0;
    private boolean rotationReadOnce = false;

    public ArduinoDataInterpreter() {
        
        serial = new SerialReader();
        serial.beginExecution();
        
    }
    
    public void updateData(){
        try{
            currentArdData = serial.getCurrentData();
            if(currentArdData == null){
                System.out.println("Waiting to receive input...");
            }else{
                if(!currentArdData.equals(previousArdData)){
                    System.out.println(currentArdData);
                    previousArdData = currentArdData;
                }
            }
        }catch(Throwable e){
            System.out.println("READING SERIAL DATA FAILED!: " + e);
        }
        
       
        
        //the time before it will start using the readings.
        // intended to give the user time to hit the probe reset
        float timeThreshold = 3000;
        
        //the measurements vary by +- 0.02 when the probe is still, 
        //  thus the threshold we care about is 0.02/100 = 0.0002
        float threshold = 0.0002f;
        if(currentArdData != null && currentArdData.getTimestamp() > timeThreshold){
            try{
                currentXangle = currentArdData.getPitch()/100.0f;
                currentYangle = currentArdData.getRoll()/100.0f;
                currentZangle = currentArdData.getYaw()/100.0f;
                if(rotationReadOnce){
                    //littleObject.rotate(0,0,-1*lastZangle);
                    //littleObject.rotate(0,-1*lastYangle,0);
                    //littleObject.rotate(-1*lastXangle,0,0);
                    
                    deltaXangle = currentXangle - lastXangle;
                    deltaYangle = currentYangle - lastYangle;
                    deltaZangle = currentZangle - lastZangle;
                    
                    if(Math.abs(deltaXangle) <= threshold){
                        deltaXangle = 0;
                    }
                    if(Math.abs(deltaYangle) <= threshold){
                        deltaYangle = 0;
                    }
                    if(Math.abs(deltaZangle) <= threshold){
                        deltaZangle = 0;
                    }
                    
                    
                    
                }else{
                    rotationReadOnce = true;
                }
                
                lastXangle = currentXangle;
                lastYangle = currentYangle;
                lastZangle = currentZangle;
                
                deltaX = currentArdData.getX();
                deltaY = currentArdData.getY();
                
                if(deltaX > 128) deltaX = deltaX - 256;
                if(deltaY > 128) deltaY = deltaY - 256;
                
                deltaX = deltaX/500.0f;
                deltaY = deltaY/500.0f;
                
            }catch(Exception e){
                System.out.println(e);
            }
            
        }
    }

    public float getDeltaXangle() {
        return deltaXangle;
    }

    public float getDeltaYangle() {
        return deltaYangle;
    }

    public float getDeltaZangle() {
        return deltaZangle;
    }

    public float getDeltaX() {
        return deltaX;
    }

    public float getDeltaY() {
        return deltaY;
    }
    
}
