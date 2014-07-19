/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataFilter;

/**
 * * UNUSED CODE:
 * KEEP IN CASE IT WILL BE USEFUL
 *      IN THE FUTURE
 *
 * @author BLI
 */
public class OrientationFilterThreshold extends OrientationFilter{
    
    //factor to multiply mean error by before processing the change
    public static final float FILTER_THRESHOLD_FACTOR = 3.0f;
    
    private float meanErrorPitch=0;
    private float meanErrorRoll=0;
    private float meanErrorYaw=0;
    
    public void setMeanErrors(float meanErrorYaw, float meanErrorPitch, float meanErrorRoll){
        this.meanErrorPitch = meanErrorPitch;
        this.meanErrorRoll = meanErrorRoll;
        this.meanErrorYaw = meanErrorYaw;
    }

    @Override
    public void filterData() {
        outputPitch = getOutputAngle(inputPitch,lastPitch,meanErrorPitch);
        outputRoll = getOutputAngle(inputRoll,lastRoll,meanErrorRoll);
        outputYaw = getOutputAngle(inputYaw,lastYaw,meanErrorYaw);
    }
    
    private float getOutputAngle(float currentAngle, float lastAngle, float meanError){
        float deltaAngle = currentAngle - lastAngle;
        if (Math.abs(deltaAngle) > meanError*FILTER_THRESHOLD_FACTOR){
            return currentAngle;
        }else{
            return lastAngle;
        }
    }
    
}
