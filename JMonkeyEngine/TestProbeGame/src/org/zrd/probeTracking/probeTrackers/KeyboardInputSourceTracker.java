/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking.probeTrackers;

import com.jme3.input.InputManager;
import org.zrd.keyboardObjectTracking.keyboardTrackingReadImpl.KeyboardTrackingImpl;

/**
 *
 * @author BLI
 */
public class KeyboardInputSourceTracker implements AbstractInputSourceTracker{
    
    private KeyboardTrackingImpl keyboardInputTracker;
    
    public KeyboardInputSourceTracker(InputManager manager){
        keyboardInputTracker = new KeyboardTrackingImpl(manager);
    }

    public float getCurrentYawRadians() {
        return keyboardInputTracker.getCurrentAngles().getCurrentYaw();
    }

    public float getCurrentPitchRadians() {
        return keyboardInputTracker.getCurrentAngles().getCurrentPitch();
    }

    public float getCurrentRollRadians() {
        return keyboardInputTracker.getCurrentAngles().getCurrentRoll();
    }

    public float getDeltaX() {
        return keyboardInputTracker.getCurrentPosChange().getXDisp();
    }

    public float getDeltaY() {
        return keyboardInputTracker.getCurrentPosChange().getYDisp();
    }

    public boolean canBeginTracking() {
        return true;
    }

    public void updateData() {
    }

    public boolean isCalibrating() {
        return false;
    }

    public void startStopCalibration() {
    }

    public void setFilterMode(int mode) {
    }
    
}
