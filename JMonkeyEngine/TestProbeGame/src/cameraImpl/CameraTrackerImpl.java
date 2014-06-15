/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraImpl;

import camera.CameraTracker;
import cameraImpl.CameraMovementImpl;
import cameraImpl.CameraRadialMovementImpl;
import cameraImpl.CameraRotateImpl;
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
    
    private static final float rotationAmount = 1.0f/20.0f;
    private static final float rotationAmountNeg = -1*rotationAmount;
    
    private static Vector3f leftRightAxis = Vector3f.UNIT_Y;
    private static Vector3f upDownAxis = Vector3f.UNIT_X;
    
    private InputManager inputMang;
    
    public CameraTrackerImpl(Camera currentCam, FlyByCamera currentFlyCam, InputManager manager){
        this.currentCam = currentCam;
        this.currentFlyCam = currentFlyCam;
        this.inputMang = manager;
        addListeners();
        enableDefaults();
    }
    
    private void addRotateListener(String name, int mouseCode, boolean dir, float rotAmount,Vector3f rotAxis){
        Matrix3f rotMatrix = GeneralHelper.getRotationMatrix(rotAmount, rotAxis);
        new CameraRotateImpl(inputMang,name,mouseCode,dir,currentCam,rotMatrix,lookAtCenter);
    }
    protected void rotateUp(){
        addRotateListener("rotCameraUp",MouseInput.AXIS_Y,false,rotationAmountNeg, upDownAxis);
    }
    protected void rotateDown(){
        addRotateListener("rotCameraDown",MouseInput.AXIS_Y,true,rotationAmount, upDownAxis);
    }
    protected void rotateLeft(){
        addRotateListener("rotCameraLeft",MouseInput.AXIS_X,false,rotationAmountNeg, leftRightAxis);
    }
    protected void rotateRight(){
        addRotateListener("rotCameraRight",MouseInput.AXIS_X,true,rotationAmount, leftRightAxis);
    }
    
    private void addMovementListener(String name,int keyCode){
        new CameraMovementImpl(inputMang,name,keyCode,currentCam);
    }
    protected void moveUp(){
        addMovementListener("moveCameraUp",KeyInput.KEY_UP);
    }
    protected void moveDown(){
        addMovementListener("moveCameraDown",KeyInput.KEY_DOWN);
    }
    protected void moveLeft(){
        addMovementListener("moveCameraLeft",KeyInput.KEY_LEFT);
    }
    protected void moveRight(){
        addMovementListener("moveCameraRight",KeyInput.KEY_RIGHT);
    }
    
    private void addRadialMovementListener(String name, int keyCode, boolean inward){
        new CameraRadialMovementImpl(inputMang,name,keyCode,currentCam,lookAtCenter,inward);
    }
    protected void moveInward(){
        addRadialMovementListener("moveInward",KeyInput.KEY_R,true);
    }
    protected void moveOutward(){
        addRadialMovementListener("moveOutward",KeyInput.KEY_F,false);
    }
    
    protected void setDefaultCamera(){
        setDefaultCamera(false);        
    }
    
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
        
    }
    
    protected void enableFlyCam(){
        currentFlyCam.setEnabled(true);
        currentFlyCam.setDragToRotate(true);
        currentFlyCam.setMoveSpeed(10f);
        currentFlyCam.setRotationSpeed(0f);
    }
    
}
