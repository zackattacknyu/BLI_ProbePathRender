/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.keyboardTrackingRead;

/**
 * Interface for the angle change that is caused by keyboard input
 *
 * @author BLI
 */
public interface AngleChange {
    
    /**
     * The angle change in radians from a single keystroke
     */
    public static final float ANGLE_CHANGE_RADIANS = 1.0f/20.0f;
    
    /**
     * When rotating positively, this is the factor
     */
    public static final float POS_FACTOR = 1.0F;
    
    /**
     * When rotating negatively, this is the factor
     */
    public static final float NEG_FACTOR = -1.0F;
    
    /**
     * Says that type of angle we are moving by
     */
    public enum ANGLE_TYPE{ YAW, PITCH, ROLL};
    
    /**
     * changes the current pitch
     * @param change angle change to add to yaw
     */
    void changeYaw(float change);
    
    /**
     * changes the current pitch
     * @param change angle change to add to roll
     */
    void changeRoll(float change);
    
    /**
     * changes the current pitch
     * @param change angle change to add to pitch
     */
    void changePitch(float change);
    
}
