/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package camera;

import com.jme3.math.Vector3f;

/**
 *
 * @author Zach
 */
public interface CameraRotate {
    
    /**
     * Axis to rotate by when doing up/down mouse moves
     */
    public static final Vector3f UP_DOWN_AXIS = Vector3f.UNIT_X;
    
    /**
     * Axis to rotate by when doing left/right mouse moves
     */
    public static final Vector3f LEFT_RIGHT_AXIS = Vector3f.UNIT_Y;
    
    /**
     * angles to rotate by during individual mouse movements
     */
    public static final float ROTATION_AMOUNT_NEG = -1.0F / 20.0F;
    public static final float ROTATION_AMOUNT = 1.0F / 20.0F;
    
    /**
     * Says whether the rotate should occur, i.e. if 
     *  mouse is pressed down or not
     * @return      whether or not to go through with rotation
     */
    boolean shouldRotate();
    
    /**
     * Changes the camera's location as per the rotation
     */
    void changeLocation();
    
    /**
     * Ensures the lookAt location is intact
     */
    void changeLookAt();
    
}
