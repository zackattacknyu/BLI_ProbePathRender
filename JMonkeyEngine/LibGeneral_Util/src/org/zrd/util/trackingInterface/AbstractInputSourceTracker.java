/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.trackingInterface;

/**
 * This is an abstract interface for all the classes which read probe
 *      position data. The object tracker that uses keyboard input
 *      and the one that use serial input to read the probe position
 *      both implement this interface.
 *
 * @author BLI
 */
public interface AbstractInputSourceTracker {
    
    float getTrackingQuality();
    
    /**
     * Gets the current yaw from the source tracker
     * @return      current yaw reading
     */
    float getCurrentYawRadians();
    
    /**
     * Gets the current pitch from the source tracker
     * @return      current pitch reading
     */
    float getCurrentPitchRadians();
    
    /**
     * Gets the current roll from the source tracker
     * @return      current roll reading
     */
    float getCurrentRollRadians();
    
    
    /**
     * Gets the current delta x from the source tracker
     * @return      current delta x reading
     */
    float getDeltaX();
    
    /**
     * Gets the current delta y from the source tracker
     * @return      current delta y reading
     */
    float getDeltaY();
    
    
    /**
     * Updates the source tracker. Meant to be called many times
     *      in a second
     */
    void updateData();

    /**
     * Resets the source tracker
     */
    void resetProbeReader();
}
