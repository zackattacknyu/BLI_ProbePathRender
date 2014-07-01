/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.keyboardTrackingRead;

/**
 *
 * @author BLI
 */
public interface AngleChange {
    
    public static final float ANGLE_CHANGE_RADIANS = 1.0f/20.0f;
    
    public static final float POS_FACTOR = 1.0F;
    public static final float NEG_FACTOR = -1.0F;
    
    public enum ANGLE_TYPE{ YAW, PITCH, ROLL};
    
    void changeYaw(float factor);
    void changeRoll(float factor);
    void changePitch(float factor);
    
}
