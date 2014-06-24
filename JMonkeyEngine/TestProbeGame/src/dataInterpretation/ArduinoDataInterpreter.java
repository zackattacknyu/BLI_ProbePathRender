/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataInterpretation;

import dataReaderImpl.SerialReader;
import util.PropertiesHelper;
import com.jme3.math.FastMath;
import java.util.HashMap;
import java.util.Properties;
import dataInterpretation.SerialDataPoint;
import dataInterpretation.DataSet;
import dataInterpretation.LowPassFilterData;

/**
 * 
 * @author BLI
 */
public class ArduinoDataInterpreter {
    
    //see above for explanation of stages
    private int currentStage = 1;
    
    private SerialReader serial;
    private SerialDataPoint currentSerialData;
    //private SerialDataPoint lastArdData;
    //private SerialDataPoint previousArdData;
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
    private float currentYaw=0,currentPitch=0,currentRoll=0;
    private float firstYaw=0,firstPitch=0,firstRoll=0;
    private float outputYawRadians=0,outputPitchRadians=0,outputRollRadians=0;
    private String currentSerialOutput, previousSerialOutput;
    
    private boolean useLowPassFilterData = false;
    
    //public static final float degreeToRadianFactor = (float)(Math.PI/180.0);
    
    //flag for only showing output and not processing it
    private boolean onlyDoOutput;
    
    //flag for whether or not to parse the output. If not parsed, raw string is shown
    private boolean parseOutput;
    
    private boolean updateExists = false;
    private boolean showOutput;
    
    private boolean calibrating = false;
    private boolean calibrated = false;
    
    private float meanErrorPitch = 0;
    private float meanErrorYaw = 0;
    private float meanErrorRoll = 0;
    
    //set to 1 to use threshold factor
    //set to 0 to not use threshold factor
    private float rawSwitch = 1;
    
    //this is the number of initial estimated data points for calibration
    private static final int NUMBER_INIT_CALIB_ELEMENTS = 100;
    
    private DataSet initYawData;
    private DataSet initPitchData;
    private DataSet initRollData;
    private DataSet initXData;
    private DataSet initYData;
    
    private LowPassFilterData yawData;
    
    private static HashMap<String,Integer> dataLocations;
    private Properties trackerProps;
    
    //factor to multiply mean error by before processing the change
    private float thresholdFactor = 3.0f;

    public ArduinoDataInterpreter() {
        trackerProps = PropertiesHelper.getProperties();
        
        dataLocations = DataLocationsMap.getDataLocationMap(trackerProps);
        try{
            serial = new SerialReader();
            serial.beginExecution();
        }catch(Throwable t){
            System.out.println("CANNOT USE SERIAL DEVICE. INSTALL RXTX.");
            System.out.println(t);
        }
        
        System.out.println("Waiting to receive input...");
        
        onlyDoOutput = Boolean.parseBoolean(
                trackerProps.getProperty(
                "arduinoData.onlyDoOutput"));
        
        parseOutput = Boolean.parseBoolean(
                trackerProps.getProperty(
                "arduinoData.parseOutput"));
        
        showOutput = Boolean.parseBoolean(
                trackerProps.getProperty(
                "arduinoData.showOutput"));
        
        
        
    }
    
    private void readSerialData(){
        updateExists = false;
        try{
            
            currentSerialOutput = serial.getCurrentOutput();
            if(!String.valueOf(currentSerialOutput).equals("null")
                    && !currentSerialOutput.equals(previousSerialOutput)){
                
                if(parseOutput){
                    currentSerialData = new SerialDataPoint(currentSerialOutput,dataLocations);
                }
                
                //shows the current output
                if(showOutput){
                    
                    if(parseOutput){
                        //if parsing, show the result
                        System.out.println(currentSerialData);
                    }else{
                        //if no parsing, just show raw output
                        System.out.println(currentSerialOutput);
                    }
                    
                }
                
                updateExists = true;
                previousSerialOutput = currentSerialOutput;
            }
            
        }catch(Throwable e){
            System.out.println("READING SERIAL DATA FAILED!: " + e);
        }
    }

    public void setRawSwitch(float rawSwitch) {
        this.rawSwitch = rawSwitch;
    }

    public void setUseLowPassFilterData(boolean useLowPassFilterData) {
        this.useLowPassFilterData = useLowPassFilterData;
    }

    public String getCurrentSerialOutput() {
        return currentSerialOutput;
    }
    
    private void processArdData(){
        
        if(updateExists){
            
            if(calibrating){
                processCurrentCalibrationPoint();
            }else if(calibrated){
                processObjectUpdate();
            }
            
        }
        
        
        
    }
    
    private void processCurrentCalibrationPoint(){
        
        initYawData.addToDataSet(currentSerialData.getYaw());
        initPitchData.addToDataSet(currentSerialData.getPitch());
        initRollData.addToDataSet(currentSerialData.getRoll());
        initXData.addToDataSet(currentSerialData.getX());
        initYData.addToDataSet(currentSerialData.getY());
        yawData.addToData(currentSerialData.getYaw());
        
    }
    
    private void processCalibration(){
        
        initYawData.processData();
        initPitchData.processData();
        initRollData.processData();
        initXData.processData();
        initYData.processData();
        
        lastPitch = currentSerialData.getPitch();
        lastRoll = currentSerialData.getRoll();
        lastYaw = currentSerialData.getYaw();
        
        currentPitch = lastPitch;
        currentRoll = lastRoll;
        currentYaw = lastYaw;
        
        firstPitch = lastPitch;
        firstRoll = lastRoll;
        firstYaw = lastYaw;
        
        meanErrorPitch = initPitchData.getMeanError();
        meanErrorRoll = initRollData.getMeanError();
        meanErrorYaw = initYawData.getMeanError();
        

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
        deltaX = currentSerialData.getX();
        deltaY = currentSerialData.getY();

        deltaX = deltaX/1000.0f;
        deltaY = deltaY/1000.0f;
    }
    
    private void processYawPitchRoll(){
        
        float pitch = currentSerialData.getPitch();
        float roll = currentSerialData.getRoll();
        float yaw = currentSerialData.getYaw();
        
        yawData.addToData(yaw);
        
        deltaPitch = pitch - lastPitch;
        deltaRoll = roll - lastRoll;
        deltaYaw = yaw - lastYaw;

        if(Math.abs(deltaPitch) > thresholdFactor*meanErrorPitch*rawSwitch){
            currentPitch = pitch;
            outputPitchRadians = getEulerAngle(pitch-firstPitch);
        }
        if(Math.abs(deltaRoll) > thresholdFactor*meanErrorRoll*rawSwitch){
            currentRoll = roll;
            outputRollRadians = getEulerAngle(roll-firstRoll);
        }
        
        if(Math.abs(deltaYaw) > thresholdFactor*meanErrorYaw*rawSwitch){
            
            if(useLowPassFilterData){
                outputYawRadians = getEulerAngle(yawData.getAverage()-firstYaw);
            }else{
                outputYawRadians = getEulerAngle(yaw-firstYaw);
            }
            
            
            
            currentYaw = yaw;
        }
    }

    public boolean isCalibrated() {
        return calibrated;
    }
    
    
    
    //converts degrees, which the data is in, to radians, which is used
    //  by JMonkeyEngine for the rotation
    private float getEulerAngle(float degrees){
        return degrees*FastMath.DEG_TO_RAD;
    }

    public float getOutputYawRadians() {
        return outputYawRadians;
    }

    public float getOutputPitchRadians() {
        return outputPitchRadians;
    }

    public float getOutputRollRadians() {
        return outputRollRadians;
    }
    
    
    public void startStopCalibration(){
        
        calibrating = !calibrating; 
        
        if(calibrating){
            
            //start the calibration code
            initYawData = new DataSet(NUMBER_INIT_CALIB_ELEMENTS);
            initPitchData = new DataSet(NUMBER_INIT_CALIB_ELEMENTS);
            initRollData = new DataSet(NUMBER_INIT_CALIB_ELEMENTS);
            initXData = new DataSet(NUMBER_INIT_CALIB_ELEMENTS);
            initYData = new DataSet(NUMBER_INIT_CALIB_ELEMENTS);
            yawData = new LowPassFilterData(3);
            
        }else{
            
            //end the calibration
            calibrated = true;
            processCalibration();
            displayCalibrationResults();
        }
        
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

    public boolean isCalibrating() {
        return calibrating;
    }
    
    public float getDeltaX() {
        return deltaX;
    }

    public float getDeltaY() {
        return deltaY;
    }
   
}
