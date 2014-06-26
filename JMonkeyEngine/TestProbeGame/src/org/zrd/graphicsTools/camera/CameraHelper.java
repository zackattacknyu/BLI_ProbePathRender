/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.graphicsTools.camera;

import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**This contains static helper methods that are used
 *      by the camera classes.
 * 
 * It should NOT contain any JMonkeyEngine dependent code
 *      as it is meant to be applied if programming 
 *      this in other platforms
 *
 * @author Zach
 */
public class CameraHelper {
    
    
    /* This block is
     * used for camera rotation
     */
    public static Vector3f getRotatedCameraLocation(Matrix3f rotMatrix, Vector3f oldLocation){
        return rotMatrix.mult(oldLocation);
    }
    public static Vector3f getLookAtCenter(Vector3f centerLoc){
        return centerLoc;
    }
    public static Vector3f getLookAtUpVector(Vector3f upVector){
        return upVector;
    }
    
    
    /*
     * This block is used
     * for radial movement
     */
    
    /**
     * This takes in the current location, current look at center, 
     *      and the move amount and moves the location that amount
     *      in the direction of the look at center
     * @param currentLoc    current location
     * @param lookAtCenter  center of look at
     * @param moveAmount    amount to move along radial line
     * @return 
     */
    public static Vector3f getNewRadialLocation(Vector3f currentLoc, 
            Vector3f lookAtCenter, float moveAmount){
        Vector3f direction = currentLoc.subtract(lookAtCenter);
        direction.normalizeLocal();
        Vector3f moveVector = direction.mult(moveAmount);
        return currentLoc.add(moveVector);
    }
    
    /*
     * This next block is used for the camera movement methods
     *      which rotate the camera itself 
     * 
     * Most of this code is lifted directly from the FlyByCamera
     *      code in JMonkeyEngine
     */
    public static Quaternion getRotationAxes(float angle, Vector3f rotAxis, Vector3f upAxis, Vector3f leftAxis, Vector3f cameraDir){
        Matrix3f mat = new Matrix3f();
        mat.fromAngleNormalAxis(angle, rotAxis);

        mat.mult(upAxis, upAxis);
        mat.mult(leftAxis, leftAxis);
        mat.mult(cameraDir, cameraDir);

        Quaternion q = new Quaternion();
        q.fromAxes(leftAxis, upAxis, cameraDir);
        q.normalizeLocal();
        return q;
    }
    
    
}
