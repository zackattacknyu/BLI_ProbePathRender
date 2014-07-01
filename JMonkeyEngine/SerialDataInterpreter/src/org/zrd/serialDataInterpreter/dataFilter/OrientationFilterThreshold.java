/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataFilter;

import org.zrd.serialDataInterpreter.dataFilter.OrientationFilter;

/**
 *
 * @author BLI
 */
public class OrientationFilterThreshold extends OrientationFilter{
    
    //factor to multiply mean error by before processing the change
    public static final float FILTER_THRESHOLD_FACTOR = 3.0f;
    
    private float meanErrorPitch;
    private float meanErrorRoll;
    private float meanErrorYaw;
    
    public OrientationFilterThreshold(
            float firstPitch, float firstYaw, float firstRoll, 
            float meanErrorPitch, float meanErrorYaw, float meanErrorRoll){
        super(firstPitch,firstYaw,firstRoll);
        this.meanErrorPitch = meanErrorPitch;
        this.meanErrorRoll = meanErrorRoll;
        this.meanErrorYaw = meanErrorYaw;
    }

    @Override
    public void filterData() {
        outputPitch = getOutputAngle(inputPitch,lastPitch,meanErrorPitch,firstPitch);
        outputRoll = getOutputAngle(inputRoll,lastRoll,meanErrorRoll,firstRoll);
        outputYaw = getOutputAngle(inputYaw,lastYaw,meanErrorYaw,firstYaw);
    }
    
    private float getOutputAngle(float currentAngle, float lastAngle, float meanError, float firstAngle){
        float deltaAngle = currentAngle - lastAngle;
        if (Math.abs(deltaAngle) > meanError*FILTER_THRESHOLD_FACTOR){
            return currentAngle - firstAngle;
        }else{
            return lastAngle;
        }
    }
    
}
