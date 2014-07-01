/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.keyboardObjectTracking.keyboardTrackingRead;

/**
 * This is the interface for position changes via the keyboard
 *
 * @author BLI
 */
public interface PositionChange {
    
    /**
     * This says the axis of displacement
     */
    public enum DISP_AXIS {X,Y};
    
    /**
     * This is the magnitude of the position change
     *      from one keystroke on the keyboard
     */
    public static final float POSITION_CHANGE = 4.0f;
    
    /**
     * Factor used if moving up or right
     */
    public static final float POS_FACTOR = 1.0f;
    
    /**
     * Factor used if moving down or left
     */
    public static final float NEG_FACTOR = -1.0f;
    
    /**
     * method that changes the current x displacement
     * @param change    amount of x change
     */
    void changeX(float change);
    
    /**
     * method that changes the current y displacement
     * @param change    amount of y change
     */
    void changeY(float change);
    
}
