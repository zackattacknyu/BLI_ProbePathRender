/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.cameraTracker.cameraMoveImpl;

import org.zrd.cameraTracker.cameraMoves.CameraRotate;
import org.zrd.cameraTracker.cameraMoves.CameraTracker;
import org.zrd.cameraTracker.cameraMoves.CameraViewpointRotate;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 *
 * @author BLI
 */
public class CameraTrackerImpl extends CameraTracker{
    
    //mouse input codes for mouse axes used in rotation
    public static final int ROTATE_UPDOWN_MOUSEAXIS = MouseInput.AXIS_Y;
    public static final int ROTATE_LEFTRIGHT_MOUSEAXIS = MouseInput.AXIS_X;
    
    //used to denote keycodes for rotating the camera viewpoint up,down,left, or right
    public static final int ROTATE_VIEWPOINT_UP_KEY = KeyInput.KEY_UP;
    public static final int ROTATE_VIEWPOINT_DOWN_KEY = KeyInput.KEY_DOWN;
    public static final int ROTATE_VIEWPOINT_LEFT_KEY = KeyInput.KEY_LEFT;
    public static final int ROTATE_VIEWPOINT_RIGHT_KEY = KeyInput.KEY_RIGHT;
    
    //used for keycodes for radial movment
    public static final int RADIAL_MOVEMENT_INWARD_KEY = KeyInput.KEY_R;
    public static final int RADIAL_MOVEMENT_OUTWARD_KEY = KeyInput.KEY_F;
    
    //used to specify speed to move fly cam
    public static final float FLY_CAM_MOVE_SPEED = 10f;
    
    
    
    protected Camera currentCam;
    protected FlyByCamera currentFlyCam;
    
    private Vector3f lookAtCenter = Vector3f.ZERO;
    
    private InputManager inputMang;
    
    /**
     * This is used to implement all the listeners and set all the defaults
     *      with respect to the camera in JMonkeyEngine
     * @param currentCam        the current camera object
     * @param currentFlyCam     the current fly camera object
     * @param manager           the current input manager
     */
    public CameraTrackerImpl(Camera currentCam, FlyByCamera currentFlyCam, InputManager manager){
        this.currentCam = currentCam;
        this.currentFlyCam = currentFlyCam;
        this.inputMang = manager;
        addListeners();
        enableDefaults();
    }
    
    /**
     * This adds one of the rotation listeners for rotating the camera
     * @param name          name of listener
     * @param mouseCode     mouse code of mouse axis
     * @param dir           direction of mouse axis movement we care about
     * @param rotAmount     angle to rotate camera
     * @param rotAxis       axis to rotate camera about
     */
    private void addRotateListener(String name, int mouseCode, boolean dir, float rotAmount,Vector3f rotAxis){
        Matrix3f rotMatrix = getRotationMatrix(rotAmount, rotAxis);
        new CameraRotateImpl(inputMang,name,mouseCode,dir,currentCam,rotMatrix,lookAtCenter);
    }
    
    /**
     * Adds the rotation listener for rotating a camera upwards
     */
    protected void rotateUp(){
        addRotateListener(ROTATE_CAMERA_UP_LISTENER_NAME,
                ROTATE_UPDOWN_MOUSEAXIS,INVERT_Y_FOR_ROTATION, 
                CameraRotate.ROTATION_AMOUNT_NEG, CameraRotate.UP_DOWN_AXIS);
    }
    
    /**
     * Adds the rotation listener for rotating a camera downwards
     */
    protected void rotateDown(){
        addRotateListener(ROTATE_CAMERA_DOWN_LISTENER_NAME,
                ROTATE_UPDOWN_MOUSEAXIS,!INVERT_Y_FOR_ROTATION, 
                CameraRotate.ROTATION_AMOUNT, CameraRotate.UP_DOWN_AXIS);
    }
    
    /**
     * Adds the rotation listener for rotating a camera left
     */
    protected void rotateLeft(){
        addRotateListener(ROTATE_CAMERA_LEFT_LISTENER_NAME,
                ROTATE_LEFTRIGHT_MOUSEAXIS,INVERT_X_FOR_ROTATION, 
                CameraRotate.ROTATION_AMOUNT_NEG, CameraRotate.LEFT_RIGHT_AXIS);
    }
    
    /**
     * Adds the rotation listener for rotating a camera right
     */
    protected void rotateRight(){
        addRotateListener(ROTATE_CAMERA_RIGHT_LISTENER_NAME,
                ROTATE_LEFTRIGHT_MOUSEAXIS,!INVERT_X_FOR_ROTATION, 
                CameraRotate.ROTATION_AMOUNT, CameraRotate.LEFT_RIGHT_AXIS);
    }
    
    /**
     * Adds the viewpoint rotation listener for a particular viewpoint rotation
     * @param name      name of listener
     * @param keyCode   keycode of key code for particular viewpoint rotation
     * @param rotAxis   axis to rotate viewpoint by
     * @param angle     angle to rotate viewpoint by
     */
    private void addViewpointRotateListener(String name,int keyCode,Vector3f rotAxis, float angle){
        new CameraViewpointRotateImpl(inputMang,name,keyCode,currentCam,rotAxis,angle);
    }
    
    /**
     * adds the viewpoint rotation listener for rotating the viewpoint upwards
     */
    protected void rotateViewpointUp(){
        addViewpointRotateListener(ROTATE_CAMERA_VIEWPOINT_UP_LISTENER_NAME,
                ROTATE_VIEWPOINT_UP_KEY,
                currentCam.getLeft(),
                CameraViewpointRotate.VIEWPOINT_ROTATION_AMOUNT_POS);
    }
    
    /**
     * adds the viewpoint rotation listener for rotating the viewpoint downwards
     */
    protected void rotateViewpointDown(){
        addViewpointRotateListener(ROTATE_CAMERA_VIEWPOINT_DOWN_LISTENER_NAME,
                ROTATE_VIEWPOINT_DOWN_KEY,
                currentCam.getLeft(),
                CameraViewpointRotate.VIEWPOINT_ROTATION_AMOUNT_NEG);
    }
    
    /**
     * adds the viewpoint rotation listener for rotating the viewpoint left
     */
    protected void rotateViewpointLeft(){
        addViewpointRotateListener(ROTATE_CAMERA_VIEWPOINT_LEFT_LISTENER_NAME,
                ROTATE_VIEWPOINT_LEFT_KEY,
                currentCam.getUp(),
                CameraViewpointRotate.VIEWPOINT_ROTATION_AMOUNT_POS);
    }
    
    /**
     * adds the viewpoint rotation listener for rotating the viewpoint right
     */
    protected void rotateViewpointRight(){
        addViewpointRotateListener(ROTATE_CAMERA_VIEWPOINT_RIGHT_LISTENER_NAME,
                ROTATE_VIEWPOINT_RIGHT_KEY,
                currentCam.getUp(),
                CameraViewpointRotate.VIEWPOINT_ROTATION_AMOUNT_NEG);
    }
    
    /**
     * This adds the radial movement listener for moving the camera radially
     *      toward or away from a center point
     * @param name          name of listener
     * @param keyCode       keycode for particular key used to trigger action
     * @param inward        whether or not to move camera inward
     */
    private void addRadialMovementListener(String name, int keyCode, boolean inward){
        new CameraRadialMovementImpl(inputMang,name,keyCode,
                currentCam,lookAtCenter,inward);
    }
    
    /**
     * Makes the listener that moves the camera inward
     */
    protected void moveInward(){
        addRadialMovementListener(RADIAL_MOVEMENT_INWARD_LISTENER_NAME,
                RADIAL_MOVEMENT_INWARD_KEY,true);
    }
    
    /**
     * Makes the listener that moves the camera outward
     */
    protected void moveOutward(){
        addRadialMovementListener(RADIAL_MOVEMENT_OUTWARD_LISTENER_NAME,
                RADIAL_MOVEMENT_OUTWARD_KEY,false);
    }
    
    /**
     * Sets up default camera
     */
    public void setDefaultCamera(){
        setDefaultCamera(new Vector3f(),new Quaternion());
    }
    
    /**
     * Sets up default camera depending on the mode we are in.
     *  By default it does nothing. It is meant to be overridden
     * @param sphereOn 
     */
    public void setDefaultCamera(short mode){
    }
    
    /**
     * Sets default camera location.
     * By default nothing happens. should be overridden
     * @param defaultLocation       initial camera location
     * @param defaultRotation       initial camera rotation
     */
    public void setDefaultCamera(Vector3f defaultLocation, Quaternion defaultRotation){
    }
    
    /**
     * Enables the flyCam, which is a JME convience method to enable
     *  w,a,s,d,q,z movement of the camera
     */
    protected void enableFlyCam(){
        currentFlyCam.setEnabled(true);
        currentFlyCam.setDragToRotate(true); //if not specified as true, cursor won't appear
        currentFlyCam.setMoveSpeed(FLY_CAM_MOVE_SPEED);
        currentFlyCam.setRotationSpeed(0f); //rotation is handled elsewhere, hence the zero
    }
    
    public static Matrix3f getRotationMatrix(float angle, Vector3f axis) {
        Quaternion rotation = new Quaternion();
        rotation.fromAngleAxis(angle, axis);
        return rotation.toRotationMatrix();
    }
    
}
