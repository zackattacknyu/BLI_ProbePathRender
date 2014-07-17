/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.trackingInterface;

/**
 *
 * @author BLI
 */
public interface AbstractInputSourceTracker {
    
    float getCurrentYawRadians();
    float getCurrentPitchRadians();
    float getCurrentRollRadians();
    
    float getDeltaX();
    float getDeltaY();
    
    void updateData();

    void resetProbeReader();
}
