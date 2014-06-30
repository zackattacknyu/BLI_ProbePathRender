/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.keyboardTrackingRead;

/**
 *
 * @author Zach
 */
public class KeyboardInputAngles {

    private float currentPitch;
    private float currentYaw;
    private float currentRoll;
    
    public static final float ANGLE_CHANGE = 1.0f/20.0f;
    
    public KeyboardInputAngles(){
        currentPitch = 0;
        currentYaw = 0;
        currentRoll = 0;
    }
    
    public void changePitch(float factor){
        currentPitch = getNewAngleValue(currentPitch,factor);
    }
    
    public void changeRoll(float factor){
        currentRoll = getNewAngleValue(currentRoll,factor);
    }
    
    public void changeYaw(float factor){
        currentYaw = getNewAngleValue(currentYaw,factor);
    }
    
    private float getNewAngleValue(float startValue, float factor){
        return startValue + factor*ANGLE_CHANGE;
    }

    public float getCurrentPitch() {
        return currentPitch;
    }

    public float getCurrentYaw() {
        return currentYaw;
    }

    public float getCurrentRoll() {
        return currentRoll;
    }
    
    
    
    
    
}
