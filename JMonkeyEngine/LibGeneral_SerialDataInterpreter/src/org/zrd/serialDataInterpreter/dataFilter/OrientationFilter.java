/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataFilter;

/**
 * 
 * UNUSED CODE:
 * KEEP IN CASE IT WILL BE USEFUL
 *      IN THE FUTURE
 *
 * @author BLI
 */
public abstract class OrientationFilter {
    
    
    protected float inputPitch,inputYaw,inputRoll;
    protected float outputPitch,outputYaw,outputRoll;

    protected float lastPitch=0, lastYaw=0, lastRoll=0;
    
    public void addDataToFilter(float pitch, float yaw, float roll){
        inputPitch = pitch;
        inputRoll = roll;
        inputYaw = yaw;
        
        filterData();
        
        lastPitch = outputPitch;
        lastYaw = outputYaw;
        lastRoll = outputRoll;
    }
    
    public abstract void filterData();

    public float getOutputPitch() {
        return outputPitch;
    }

    public float getOutputYaw() {
        return outputYaw;
    }

    public float getOutputRoll() {
        return outputRoll;
    }
    
    
    
    
}
