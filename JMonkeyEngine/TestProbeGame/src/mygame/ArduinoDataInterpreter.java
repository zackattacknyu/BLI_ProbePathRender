/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.FastMath;

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
 * 
 * @author BLI
 */
public class ArduinoDataInterpreter {
    
    //see above for explanation of stages
    private int currentStage = 1;
    
    private SerialReader serial;
    private ArduinoDataPoint currentArdData;
    private ArduinoDataPoint lastArdData;
    private ArduinoDataPoint previousArdData;
    float deltaXangle=0;
    float deltaYangle=0;
    float deltaZangle=0; 
    float deltaX = 0;
    float deltaY = 0;
    float deltaPitch = 0;
    float deltaRoll = 0;
    float deltaYaw = 0;
    float lastPitch = 0;
    float lastRoll = 0;
    float lastYaw = 0;
    
    public static final float degreeToRadianFactor = (float)(Math.PI/180.0);
    
    //flag for only showing output and not processing it
    private boolean onlyDoOutput = true;

    //flag for determining if calibration will be done first
    //only relevant if onlyDoOutput is set to false
    private boolean doCalibration = true;
    
    
    private boolean updateExists = false;
    private boolean showOutput = true;
    private boolean stage3preCalibMessageShown = false;
    private boolean stage3calibMessageShown = false;
    private boolean stage4initMessageShown = false;
    private boolean rotationReadOnce = false;
    
    /*
     * When callibrating the data,
     *  the data will be from start time to end
     *  time. The estimated time between reads
     *  is used to help initialize the data set
     *  used to process the raw data.
     */
    private float calibStartTimeMs = 1000;
    private float calibEndTimeMs = 6000;
    private float estTimeBetweenReads = 20;
    private int numberElements;
    
    //when it should process raw object updates if no calibration is done
    private float rawProcessingStartTime = 3000;
    
    private DataSet initYawData;
    private DataSet initPitchData;
    private DataSet initRollData;
    private DataSet initXData;
    private DataSet initYData;
    
    //factor to multiply mean error by before processing the change
    //private float thresholdFactor = 5.0f;
    private float thresholdFactor = 3.0f;

    public ArduinoDataInterpreter() {
        
        serial = new SerialReader();
        serial.beginExecution();
        System.out.println("Waiting to receive input...");
        
        numberElements = (int) ((calibEndTimeMs-calibStartTimeMs)/estTimeBetweenReads);
        
        initYawData = new DataSet(numberElements);
        initPitchData = new DataSet(numberElements);
        initRollData = new DataSet(numberElements);
        initXData = new DataSet(numberElements);
        initYData = new DataSet(numberElements);
        
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
            
            if(doCalibration){
                switch(currentStage){

                    case 1:
                        showInitMessage();
                        showOutput = false;
                        currentStage = 2;
                        break;
                    case 2:

                        //reset was pressed
                        if(previousArdData.getTimestamp() > currentArdData.getTimestamp()){
                            showResetMessage();
                            currentStage = 3;
                        }

                        break;

                    case 3:

                        processCurrentCalibrationPoint();

                        if(currentArdData.getTimestamp() >= calibStartTimeMs){
                            showCalibMessage();
                            if(currentArdData.getTimestamp() <= calibEndTimeMs){
                                processCurrentCalibrationPoint();
                            }else{
                                processCalibration();
                                displayCalibrationResults();
                                currentStage = 4;
                            }
                        }else{
                            showPreCalibMessage();
                        }

                        break;
                    case 4:
                        showStage4Message();
                        processObjectUpdate();
                        //showOutput = true;
                        break;
                }
            }else{
                if(currentArdData.getTimestamp() >= rawProcessingStartTime){
                    processObjectUpdate();
                }
                
            }
            
            
            previousArdData = currentArdData;
            
        }
        
        
        
    }
    
    private void showStage4Message(){
        if(!stage4initMessageShown){
            System.out.println();
            System.out.println("Now Processing Probe Data");
            stage4initMessageShown = true;
        }
    }
    
    private void processCurrentCalibrationPoint(){
        
        initYawData.addToDataSet(currentArdData.getYaw());
        initPitchData.addToDataSet(currentArdData.getPitch());
        initRollData.addToDataSet(currentArdData.getRoll());
        initXData.addToDataSet(currentArdData.getX());
        initYData.addToDataSet(currentArdData.getY());
        
    }
    
    private void processCalibration(){
        
        initYawData.processData();
        initPitchData.processData();
        initRollData.processData();
        initXData.processData();
        initYData.processData();
        
        lastPitch = currentArdData.getPitch();
        lastRoll = currentArdData.getRoll();
        lastYaw = currentArdData.getYaw();
        

    }
    

    
    public void updateData(){
        
        readSerialData();
        
        if(!onlyDoOutput){
            processArdData();
        }
    }
    
    private void processObjectUpdate(){
        
        processYawPitchRoll();
        processXYdata();

    }
    
    private void processXYdata(){
        deltaX = currentArdData.getX();
        deltaY = currentArdData.getY();

        /*
         * TODO: Comment these lines and re-test
         */
        //if(deltaX > 128) deltaX = deltaX - 256;
        //if(deltaY > 128) deltaY = deltaY - 256;

        deltaX = deltaX/1000.0f;
        deltaY = deltaY/1000.0f;
    }
    
    private void processYawPitchRoll(){
        
        deltaPitch = currentArdData.getPitch() - lastPitch;
        deltaRoll = currentArdData.getRoll() - lastRoll;
        deltaYaw = currentArdData.getYaw() - lastYaw;

        if(doCalibration && Math.abs(deltaPitch) <= thresholdFactor*initPitchData.getMeanError()){
            deltaXangle = 0;
        }else{
            deltaXangle = getEulerAngle(deltaPitch);
            //System.out.println("Delta X angle:" + deltaXangle);
            lastPitch = currentArdData.getPitch();
        }
        
        if(doCalibration && Math.abs(deltaRoll) <= thresholdFactor*initRollData.getMeanError()){
            deltaYangle = 0;
        }else{
            deltaYangle = getEulerAngle(deltaRoll);
            //System.out.println("Delta Y angle:" + deltaYangle);
            lastRoll = currentArdData.getRoll();
        }
        
        if(doCalibration && Math.abs(deltaYaw) <= thresholdFactor*initYawData.getMeanError()){
            deltaZangle = 0;
        }else{
            deltaZangle = getEulerAngle(deltaYaw);
//            System.out.println("deltaYaw:" + deltaYaw);
//            System.out.println("MeanYawError:" + initYawData.getMeanError());
//            System.out.println("Delta Z angle:" + deltaZangle);
            lastYaw = currentArdData.getYaw();
        }
    }
    
    //converts degrees, which the data is in, to radians, which is used
    //  by JMonkeyEngine for the rotation
    private float getEulerAngle(float degrees){
        return degrees*FastMath.DEG_TO_RAD;
    }
    
    public float getCurrentYaw(){
        if(currentStage > 3){
            return getEulerAngle(currentArdData.getYaw()-initYawData.getMean());
        }else if(!doCalibration){
            return getEulerAngle(currentArdData.getYaw());
        }else{
            return 0;
        }
        
    }
    
    
    
    private void showInitMessage(){

        System.out.println("Data is being received");
        System.out.println();
        System.out.println("Now hit the Reset Button");
    }
    
    private void showCalibMessage(){
        
        if(!stage3calibMessageShown){
                            
            System.out.println();
            System.out.println("Now calibrating the probe");
            System.out.println("Do not move the probe");
            stage3calibMessageShown = true;

        }
        
    }
    
    private void showPreCalibMessage(){
        if(!stage3preCalibMessageShown){
            System.out.println("Waiting to calibrate the probe");
            stage3preCalibMessageShown = true;
        }
        
    }
    private void showResetMessage(){
        System.out.println();
        System.out.println("Resetting the stream");
    }
    
    private void displayCalibrationResults(){
        System.out.println("Init Data Established");
        System.out.println(
            "Mean: yaw=" + initYawData.getMean()
            + ", pitch=" + initPitchData.getMean()
            + ", roll=" + initRollData.getMean()
            + ", x=" + initXData.getMean()
            + ", y=" + initYData.getMean()
                );
        System.out.println(
            "Mean Error: yaw=" + initYawData.getMeanError()
            + ", pitch=" + initPitchData.getMeanError()
            + ", roll=" + initRollData.getMeanError()
            + ", x=" + initXData.getMeanError()
            + ", y=" + initYData.getMeanError()
                );
        System.out.println(
            "Mean Squared Error: yaw=" + initYawData.getMeanSquaredError()
            + ", pitch=" + initPitchData.getMeanSquaredError()
            + ", roll=" + initRollData.getMeanSquaredError()
            + ", x=" + initXData.getMeanSquaredError()
            + ", y=" + initYData.getMeanSquaredError()
                );
        
    }

    public void setOnlyDoOutput(boolean onlyDoOutput) {
        this.onlyDoOutput = onlyDoOutput;
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
    
    public float getCurrentTimestamp(){
        if(currentArdData == null){
            return 0;
        }else{
            return currentArdData.getTimestamp();
        }
        
    }
}
