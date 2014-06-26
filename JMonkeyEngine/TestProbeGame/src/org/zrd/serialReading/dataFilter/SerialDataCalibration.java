/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialReading.dataFilter;

import org.zrd.util.general.DataSet;
import org.zrd.serialReading.dataReader.SerialDataPoint;

/**
 *
 * @author BLI
 */
public class SerialDataCalibration {

    
    //this is the number of initial estimated data points for calibration
    private static final int NUMBER_INIT_CALIB_ELEMENTS = 100;
    
    private DataSet initYawData,initPitchData,initRollData,initXData,initYData;
    
    public SerialDataCalibration(){
        initializeCalibration(NUMBER_INIT_CALIB_ELEMENTS);
    }
    
    private void initializeCalibration(int numElementsInit){
        initYawData = new DataSet(numElementsInit);
        initPitchData = new DataSet(numElementsInit);
        initRollData = new DataSet(numElementsInit);
        initXData = new DataSet(numElementsInit);
        initYData = new DataSet(numElementsInit);
    }
    
    public void addCalibrationPoint(SerialDataPoint currentSerialData){
        initYawData.addToDataSet(currentSerialData.getYaw());
        initPitchData.addToDataSet(currentSerialData.getPitch());
        initRollData.addToDataSet(currentSerialData.getRoll());
        initXData.addToDataSet(currentSerialData.getX());
        initYData.addToDataSet(currentSerialData.getY());
    }
    
    public void finishCalibration(){
        initYawData.processData();
        initPitchData.processData();
        initRollData.processData();
        initXData.processData();
        initYData.processData();
    }
    
    public float getMeanErrorPitch(){
        return initPitchData.getMeanError();
    }
    
    public float getMeanErrorRoll(){
        return initRollData.getMeanError();
    }
    
    public float getMeanErrorYaw(){
        return initYawData.getMeanError();
    }
    
    public float getMeanPitch(){
        return initPitchData.getMean();
    }
    
    public float getMeanYaw(){
        return initYawData.getMean();
    }
    
    public float getMeanRoll(){
        return initRollData.getMean();
    }
    
    public void displayCalibrationResults(){
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
    
    
}
