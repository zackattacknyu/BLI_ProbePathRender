/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.dataHelp;

/**
 *
 * @author BLI
 */
public class BasicAngleHelper {
    
    public static final float DEG_TO_RAD_FACTOR = (float) (Math.PI/180.0);
    public static final float RAD_TO_DEG_FACTOR = (float) (180.0/Math.PI);
    
    public static float convertDegreesToRadians(float degrees){
        return degrees*DEG_TO_RAD_FACTOR;
    }
    
    public static float convertRadiansToDegrees(float radians){
        return radians*RAD_TO_DEG_FACTOR;
    }
    
}
