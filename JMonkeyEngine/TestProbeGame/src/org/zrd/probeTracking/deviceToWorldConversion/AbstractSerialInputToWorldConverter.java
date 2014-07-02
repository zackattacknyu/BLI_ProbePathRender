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
public abstract class AbstractSerialInputToWorldConverter {

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
    
    /*
     * These vectors are the initial x and y displacement vectors.
     *      The subclasses of this one are in charge of manipulating
     *      these vectors based on rotation to get the actual
     *      x, y, and z displacment
     */
    
    //vector for x displacement
    public static final Vector3f INIT_X_VECTOR = new Vector3f(1,0,0);
    
    //vector for y displacement
    public static final Vector3f INIT_Y_VECTOR = new Vector3f(0,1,0);
    
    public Vector3f getXYZDisplacement(float deltaX, float deltaY, 
            float yaw, float pitch, float roll){
        
        float xChangeMagnitude = deltaX*scaleFactorX;
        float yChangeMagnitude = deltaY*scaleFactorY;
        
        Vector3f xDispVector = getRotatedVector(INIT_X_VECTOR,yaw,pitch,roll);
        Vector3f yDispVector = getRotatedVector(INIT_Y_VECTOR,yaw,pitch,roll);
        
        Vector3f xDisplacement = xDispVector.mult(xChangeMagnitude);
        Vector3f yDisplacement = yDispVector.mult(yChangeMagnitude);
        
        Vector3f initDisplacement = xDisplacement.add(yDisplacement);
        
        return rotationCalibrationMatrix.mult(initDisplacement);
    }
    
    protected abstract Vector3f getRotatedVector(Vector3f inputVector, 
            float yaw, float pitch, float roll);

    public void setRotationCalibrationMatrix(Matrix3f rotationCalibrationMatrix) {
        this.rotationCalibrationMatrix = rotationCalibrationMatrix;
    }

    public void setScaleFactorX(float scaleFactorX) {
        this.scaleFactorX = scaleFactorX;
    }

    public void setScaleFactorY(float scaleFactorY) {
        this.scaleFactorY = scaleFactorY;
    }
    
    
    
}
