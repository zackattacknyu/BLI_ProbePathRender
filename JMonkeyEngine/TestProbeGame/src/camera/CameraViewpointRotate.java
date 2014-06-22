/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package camera;

import com.jme3.math.Vector3f;

/** This is used to rotate the camera viewpoint itself
 *      up, down, left, or right
 *
 * @author Zach
 */
public interface CameraViewpointRotate {
    
    public static final float VIEWPOINT_ROTATION_AMOUNT = (1.0f/20.0f);
    public static final float VIEWPOINT_ROTATION_AMOUNT_NEG = (-1.0f/20.0f);
    
    public void rotateCameraViewpoint(Vector3f rotAxis, float rotAngle);
}
