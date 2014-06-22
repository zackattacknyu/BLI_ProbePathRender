/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraImpl;

import camera.CameraRotate;
import camera.CameraTracker;
import camera.CameraViewpointRotate;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import util.GeneralHelper;

/**
 *
 * @author BLI
 */
public class CameraTrackerImpl extends CameraTracker{
    
    private Camera currentCam;
    private FlyByCamera currentFlyCam;
    
    private CameraRotateImpl rotUp;
    private CameraRotateImpl rotDown;
    private CameraRotateImpl rotLeft;
    private CameraRotateImpl rotRight;
    
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
        Matrix3f rotMatrix = GeneralHelper.getRotationMatrix(rotAmount, rotAxis);
        new CameraRotateImpl(inputMang,name,mouseCode,dir,currentCam,rotMatrix,lookAtCenter);
    }
    
    /**
     * Adds the rotation listener for rotating a camera upwards
     */
    protected void rotateUp(){
        addRotateListener("rotCameraUp",MouseInput.AXIS_Y,false, 
                CameraRotate.ROTATION_AMOUNT_NEG, CameraRotate.UP_DOWN_AXIS);
    }
    
    /**
     * Adds the rotation listener for rotating a camera downwards
     */
    protected void rotateDown(){
        addRotateListener("rotCameraDown",MouseInput.AXIS_Y,true, 
                CameraRotate.ROTATION_AMOUNT, CameraRotate.UP_DOWN_AXIS);
    }
    
    /**
     * Adds the rotation listener for rotating a camera left
     */
    protected void rotateLeft(){
        addRotateListener("rotCameraLeft",MouseInput.AXIS_X,false, 
                CameraRotate.ROTATION_AMOUNT_NEG, CameraRotate.LEFT_RIGHT_AXIS);
    }
    
    /**
     * Adds the rotation listener for rotating a camera right
     */
    protected void rotateRight(){
        addRotateListener("rotCameraRight",MouseInput.AXIS_X,true, 
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
        addViewpointRotateListener("moveCameraUp",KeyInput.KEY_UP,
                currentCam.getLeft(),
                CameraViewpointRotate.VIEWPOINT_ROTATION_AMOUNT);
    }
    
    /**
     * adds the viewpoint rotation listener for rotating the viewpoint downwards
     */
    protected void rotateViewpointDown(){
        addViewpointRotateListener("moveCameraDown",KeyInput.KEY_DOWN,
                currentCam.getLeft(),
                CameraViewpointRotate.VIEWPOINT_ROTATION_AMOUNT_NEG);
    }
    
    /**
     * adds the viewpoint rotation listener for rotating the viewpoint left
     */
    protected void rotateViewpointLeft(){
        addViewpointRotateListener("moveCameraLeft",KeyInput.KEY_LEFT,
                currentCam.getUp(),
                CameraViewpointRotate.VIEWPOINT_ROTATION_AMOUNT);
    }
    
    /**
     * adds the viewpoint rotation listener for rotating the viewpoint right
     */
    protected void rotateViewpointRight(){
        addViewpointRotateListener("moveCameraRight",KeyInput.KEY_RIGHT,
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
        addRadialMovementListener("moveInward",KeyInput.KEY_R,true);
    }
    
    /**
     * Makes the listener that moves the camera outward
     */
    protected void moveOutward(){
        addRadialMovementListener("moveOutward",KeyInput.KEY_F,false);
    }
    
    /**
     * Sets up default camera
     */
    protected void setDefaultCamera(){
        setDefaultCamera(false);        
    }
    
    /**
     * Sets up default camera
     * TODO: Change this to take in mode and use that
     * @param sphereOn 
     */
    public void setDefaultCamera(boolean sphereOn){
        if(sphereOn){
            //when viewing the sphere
            currentCam.setLocation(new Vector3f(-22.649244f, -17.260416f, -67.74668f));
            currentCam.setRotation(new Quaternion(0.17899777f, 0.0838113f, 0.95806247f, -0.20748913f));
        }else{
            //settings for when viewing lola
            currentCam.setLocation(new Vector3f(-16.928802f, 23.251862f, -54.489124f));
            currentCam.setRotation(new Quaternion(0.20308718f, 0.20007013f, -0.042432234f, 0.9575631f));
        }
        
        /*
         * TODO: Add mode and make this one of the camera modes
         */
        boolean rawData = false;
        if(rawData){
            currentCam.setLocation(new Vector3f(-0.67807275f, 0.5436802f, -2.7487648f));
            currentCam.setRotation(new Quaternion(0.15252174f, -2.9824104E-4f, 0.7722618f, 0.61672425f));
        }
        
    }
    
    /**
     * Enables the flyCam, which is a JME convience method to enable
     *  w,a,s,d,q,z movement of the camera
     */
    protected void enableFlyCam(){
        currentFlyCam.setEnabled(true);
        currentFlyCam.setDragToRotate(true);
        currentFlyCam.setMoveSpeed(10f);
        currentFlyCam.setRotationSpeed(0f);
    }
    
}
