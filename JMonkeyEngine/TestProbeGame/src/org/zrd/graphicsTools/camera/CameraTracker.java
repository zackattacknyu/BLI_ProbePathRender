/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.graphicsTools.camera;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**This is an abstract class for planned camera tracker 
 *      methods. It includes adding the mouse/keyboard 
 *      listeners for the different movements and enabling
 *      the default cameras
 *
 * @author BLI
 */
public abstract class CameraTracker {
    
    /*
     * These are used to invert the y or x axis on the mouse
     *      when rotating the camera about a center point
     */
    public static final boolean INVERT_Y_FOR_ROTATION = false;
    public static final boolean INVERT_X_FOR_ROTATION = false;
    
    /**
     * The following block of 10 strings is all the listener names.
     * Uniqueness is important here
     * NOTE: Only change these strings if absolutely necessary
     */
    
    //rotates the camera up,down,left, or right
    public static final String ROTATE_CAMERA_UP_LISTENER_NAME = "rotCameraUp";
    public static final String ROTATE_CAMERA_DOWN_LISTENER_NAME = "rotCameraDown";
    public static final String ROTATE_CAMERA_LEFT_LISTENER_NAME = "rotCameraLeft";
    public static final String ROTATE_CAMERA_RIGHT_LISTENER_NAME = "rotCameraRight";
    
    //rotates the viewpoint up,down,left,or right
    public static final String ROTATE_CAMERA_VIEWPOINT_UP_LISTENER_NAME = "rotViewpointUp";
    public static final String ROTATE_CAMERA_VIEWPOINT_DOWN_LISTENER_NAME = "rotViewpointDown";
    public static final String ROTATE_CAMERA_VIEWPOINT_LEFT_LISTENER_NAME = "rotViewpointLeft";
    public static final String ROTATE_CAMERA_VIEWPOINT_RIGHT_LISTENER_NAME = "rotViewpointRight";
    
    //radial movement listener names
    public static final String RADIAL_MOVEMENT_INWARD_LISTENER_NAME = "moveCameraInward";
    public static final String RADIAL_MOVEMENT_OUTWARD_LISTENER_NAME = "moveCameraOutward";
    
    /**
     * This adds all the listeners
     */
    protected void addListeners(){
        addRotateViewpointListeners();
        addRadialMovementListeners();
        addRotationListeners();
    }
    
    /**
     * This is used to enable the default settings
     */
    protected void enableDefaults(){
        setDefaultCamera();
        enableFlyCam();
    }
    
    /**
     * This adds the rotation listeners
     */
    private void addRotationListeners(){
        rotateUp();
        rotateDown();
        rotateLeft();
        rotateRight();
    }
    
    /**
     * This rotates the camera up
     */
    protected abstract void rotateUp();
    
    /**
     * This rotates the camera down
     */
    protected abstract void rotateDown();
    
    /**
     * This rotates the camera left
     */
    protected abstract void rotateLeft();
    
    /**
     * This rotates the camera right
     */
    protected abstract void rotateRight();
    
    /**
     * This rotates the camera viewpoint up
     */
    protected abstract void rotateViewpointUp();
    
    /**
     * This rotates the camera viewpoint down
     */
    protected abstract void rotateViewpointDown();
    
    /**
     * This rotates the camera viewpoint left
     */
    protected abstract void rotateViewpointLeft();
    
    /**
     * This rotates the camera viewpoint right
     */
    protected abstract void rotateViewpointRight();
    
    /**
     * This adds the rotate movement listeners
     */
    private void addRotateViewpointListeners(){
        rotateViewpointUp(); 
        rotateViewpointDown();
        rotateViewpointLeft();
        rotateViewpointRight();
    }
    
    
    /**
     * This addds the radial movement listeners
     */
    private void addRadialMovementListeners(){
        moveInward();
        moveOutward();
    }

    /**
     * This moves the camera inward radially
     */
    protected abstract void moveInward();
    
    /**
     * This moves the camera outward radially
     */
    protected abstract void moveOutward();
    
    /**
     * This sets the default settings for the camera
     *      for the most common mode
     */
    public abstract void setDefaultCamera();
    
    /**
     * This sets the default camera depending on the mode
     * @param mode      mode depending on context
     */
    public abstract void setDefaultCamera(short mode);
    
    /**
     * This is meant to enable the fly by camera (w,a,s,d,q,z buttons)
     */
    protected abstract void enableFlyCam();
    
    /**
     * Sets the default camera based on location and rotation
     * @param defaultLocation       default location of camera
     * @param defaultRotation       default rotation of camera
     */
    public abstract void setDefaultCamera(Vector3f defaultLocation, Quaternion defaultRotation);
    
}
