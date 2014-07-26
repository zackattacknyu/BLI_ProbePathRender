/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.keyboardObjectTracking.keyboardTrackingClient;

import org.zrd.util.trackingInterface.AbstractInputSourceTracker;
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

    @Override
    public float getCurrentYawRadians() {
        return keyboardInputTracker.getCurrentAngles().getCurrentYaw();
    }

    @Override
    public float getCurrentPitchRadians() {
        return keyboardInputTracker.getCurrentAngles().getCurrentPitch();
    }

    @Override
    public float getCurrentRollRadians() {
        return keyboardInputTracker.getCurrentAngles().getCurrentRoll();
    }

    @Override
    public float getDeltaX() {
        return keyboardInputTracker.getCurrentPosChange().getXDisp();
    }

    @Override
    public float getDeltaY() {
        return keyboardInputTracker.getCurrentPosChange().getYDisp();
    }

    @Override
    public void updateData() {
    }

    @Override
    public void resetProbeReader() {
        
    }

    @Override
    public float getTrackingQuality() {
        return 120f;
    }
    
}
