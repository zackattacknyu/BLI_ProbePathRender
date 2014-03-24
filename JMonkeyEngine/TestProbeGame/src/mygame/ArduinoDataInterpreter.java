/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

/**
 *The Data Interpretation will go through 5 stages:
 * 
 * Stage 1: Wait for Input Data to come in
 *          - Passed the stage once the non-null check on Arduino data passes
 * Stage 2: Wait for Reset to be pressed
 *          - Passed the stage once timestamp has decreased
 * Stage 3: Establish the baseline
 *          - The data between timestampe 1000 and 3000 will be used for this
 * Stage 4: Read any changes
 *          - if the data comes in is greater than allowed thresholds
 * Stage 5: Reestablish the baseline
 *          - Once it is stable data
 * 
 * @author BLI
 */
public class ArduinoDataInterpreter {
    
    //see above for explanation of stages
    private int currentStage = 1;
    
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
    private boolean updateExists = false;
    private boolean showOutput = true;
    private boolean rotationReadOnce = false;
    
    //the time before it will start using the readings.
        // intended to give the user time to hit the probe reset
    private float timeThreshold = 3000;

    public ArduinoDataInterpreter() {
        
        serial = new SerialReader();
        serial.beginExecution();
        System.out.println("Waiting to receive input...");
    }
    
    private void readSerialData(){
        updateExists = false;
        try{
            currentArdData = serial.getCurrentData();
            if(currentArdData != null){
                if(!currentArdData.equals(previousArdData)){
                    
                    if(showOutput){
                        System.out.println(currentArdData);
                    }
                    updateExists = true;
                }
            }
        }catch(Throwable e){
            System.out.println("READING SERIAL DATA FAILED!: " + e);
        }
    }
    
    private void processArdData(){
        
        if(updateExists){
            
            
            //see comments at top for stage explanation
            switch(currentStage){

                case 1:
                    System.out.println("Data is being received");
                    System.out.println("Hit the Reset Button");
                    showOutput = false;
                    currentStage = 2;
                    break;
                case 2:
                    
                    //reset was pressed
                    if(previousArdData.getTimestamp() > currentArdData.getTimestamp()){
                        System.out.println("Resetting the stream");
                        currentStage = 3;
                    }
                    
                    break;
                case 3:
                    
                    showOutput = true;
                    
                    break;
                case 4:
                    
                    break;
                case 5:
                    
                    break;
            }
            
            previousArdData = currentArdData;
            
        }
        
        
        
    }
    
    public void updateData(){
        
        readSerialData();
        processArdData();
        
        
        //processObjectUpdate();
    }
    
    private void processObjectUpdate(){
        //the measurements vary by +- 0.02 when the probe is still, 
        //  thus the threshold we care about is 0.02/100 = 0.0002
        float threshold = 0.0002f;
        if(currentArdData != null && currentArdData.getTimestamp() > timeThreshold){
            try{
                currentXangle = currentArdData.getPitch()/100.0f;
                currentYangle = currentArdData.getRoll()/100.0f;
                currentZangle = currentArdData.getYaw()/100.0f;
                if(rotationReadOnce){
                    
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
