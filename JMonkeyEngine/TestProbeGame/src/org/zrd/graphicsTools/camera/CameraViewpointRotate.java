/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.graphicsTools.camera;

import com.jme3.math.Vector3f;

/** This is used to rotate the camera viewpoint itself
 *      up, down, left, or right
 *
 * @author Zach
 */
public interface CameraViewpointRotate {
    
    /**
     * These are the angles in radians that a single keystroke
     *      will cause in rotation
     */
    public static final float VIEWPOINT_ROTATION_AMOUNT_POS = (1.0f/20.0f);
    public static final float VIEWPOINT_ROTATION_AMOUNT_NEG = (-1.0f/20.0f);
    
    /**
     * This will rotate the camera viewpoint
     * @param rotAxis       dictates the axis of rotation
     * @param rotAngle      dictates the angle of rotation
     */
    public void rotateCameraViewpoint(Vector3f rotAxis, float rotAngle);
}
