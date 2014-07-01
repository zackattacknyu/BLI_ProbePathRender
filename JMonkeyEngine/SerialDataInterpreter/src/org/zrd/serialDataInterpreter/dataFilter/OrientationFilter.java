/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataFilter;

/**
 *
 * @author BLI
 */
public abstract class OrientationFilter {
    
    
    protected float inputPitch,inputYaw,inputRoll;
    protected float outputPitch,outputYaw,outputRoll;
    
    protected float firstPitch, firstYaw, firstRoll;
    protected float lastPitch, lastYaw, lastRoll;
    
    public OrientationFilter(float firstPitch, float firstYaw, float firstRoll){
        this.firstPitch = firstPitch;
        this.firstRoll = firstRoll;
        this.firstYaw = firstYaw;
        
        this.lastPitch = firstPitch;
        this.lastRoll = firstRoll;
        this.lastYaw = firstYaw;
    }
    
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
