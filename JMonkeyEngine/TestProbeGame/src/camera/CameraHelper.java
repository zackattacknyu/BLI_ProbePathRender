/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package camera;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;

/**
 *
 * @author Zach
 */
public class CameraHelper {
    
    
    /* This block is
     * used for camera rotation
     * 
     * TODO: Make these more sophisticated
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
    public static Vector3f getNewRadialLocation(Vector3f currentLoc, 
            Vector3f lookAtCenter, float moveAmount){
        Vector3f direction = currentLoc.subtract(lookAtCenter);
        direction.normalizeLocal();
        Vector3f moveVector = direction.mult(moveAmount);
        return currentLoc.add(moveVector);
    }
    
}
