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
    
    public KeyboardInputAngles(){
        currentPitch = 0;
        currentYaw = 0;
        currentRoll = 0;
    }
    
    public void changePitch(float change){
        currentPitch = currentPitch + change;
    }
    
    public void changeRoll(float change){
        currentRoll = currentRoll + change;
    }
    
    public void changeYaw(float change){
        currentYaw = currentYaw + change;
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
