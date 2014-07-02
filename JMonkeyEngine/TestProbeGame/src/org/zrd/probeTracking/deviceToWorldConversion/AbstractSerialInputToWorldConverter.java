/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking.deviceToWorldConversion;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;

/**
 * This is the general parent abstract class for 
 *      classes which take the device input 
 *      and then translate that to x,y,z displacement
 * 
 * NOTE: These classes are only meant to track displacement
 *      at an individual point in world coordinates, NOT
 *      the canonical coordinates of the device, which should 
 *      be found in another class using a translation.
 *
 * @author BLI
 */
public class AbstractSerialInputToWorldConverter {

    /*
     * The following three variables are calibration factors
     *      and should only be changed if the calibration changed
     */
    
    //device displacement to world displacement matrix
    //      should only be called after scale factors have been applied
    protected Matrix3f rotationCalibrationMatrix = new Matrix3f();
    
    //scale factor for x coordinate. should be same as one for y coordinate
    protected float scaleFactorX = 1.0f;
    
    //scale factor for y coordinate. should be same as one for x coordinate
    protected float scaleFactorY = 1.0f;
    
    public Vector3f getXYZDisplacement(float deltaX, float deltaY, float yaw, float pitch, float roll){
        return new Vector3f();
    }
    
}
