/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.probeCalibration;

import com.jme3.math.Matrix3f;

/**
 *
 * This is used to test whether the displacement should be reflected
 *      over the x or y axis, meaning that the x or y scale factor
 *      should be negated.
 * 
 * It does this by taking in the x,y, and z vectors from the probe at the 
 *      initial position. It then takes in the starting vector v for the path 
 *      as well as the starting vector v' for the calibrated path.
 * It puts v and v' into the x,y,z coordinate system. It then sees 
 *      if v' is close to v reflected over x or y.
 * 
 * If v' is close to v reflected over x, then y should be negated
 * 
 * If v' is close to v reflected over y, then x should be negated
 * 
 * This test should be done after rotation calibration to determine
 *      if the x or y displacement vector should be flipped
 * 
 * @author BLI
 */
public class ReflectionCalibration {
    
    
    private Matrix3f coordMatrix;
    
}
