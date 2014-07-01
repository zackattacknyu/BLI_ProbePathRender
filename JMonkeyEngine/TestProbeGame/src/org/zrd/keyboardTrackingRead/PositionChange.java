/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.keyboardTrackingRead;

/**
 *
 * @author BLI
 */
public interface PositionChange {
    
    public enum DISP_AXIS {X,Y};
    
    public static final float POSITION_CHANGE = 4.0f;
    
    public static final float POS_FACTOR = 1.0f;
    public static final float NEG_FACTOR = -1.0f;
    
    void changeX(float change);
    void changeY(float change);
    
}
