/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.keyboardObjectTracking.keyboardTrackingRead;

/** This keeps track of the current input angles that are obtained
 *      through keyboard input only
 *
 * @author Zach
 */
public class KeyboardInputAngles {

    private float currentPitch;
    private float currentYaw;
    private float currentRoll;
    
    /**
     * Constructs the object setting all angles to zero
     */
    public KeyboardInputAngles(){
        currentPitch = 0;
        currentYaw = 0;
        currentRoll = 0;
    }
    
    /**
     * changes the current pitch
     * @param change amount to add to pitch
     */
    public void changePitch(float change){
        currentPitch = currentPitch + change;
    }
    
    /**
     * changes the current roll
     * @param change    amount to add to roll
     */
    public void changeRoll(float change){
        currentRoll = currentRoll + change;
    }
    
    /**
     * changes the current yaw
     * @param change    amount to add to yaw
     */
    public void changeYaw(float change){
        currentYaw = currentYaw + change;
    }

    /**
     * Gets the current pitch
     * @return      current pitch angle in radians
     */
    public float getCurrentPitch() {
        return currentPitch;
    }

    /**
     * Gets the current yaw
     * @return      current yaw angle in radians
     */
    public float getCurrentYaw() {
        return currentYaw;
    }

    /**
     * Gets the current roll
     * @return      current roll angle in radians
     */
    public float getCurrentRoll() {
        return currentRoll;
    }
    
    
    
    
    
}
