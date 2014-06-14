/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package camera;

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
public class CameraTracker {
    
    private Camera currentCam;
    private FlyByCamera currentFlyCam;
    
    private CameraRotate rotUp;
    private CameraRotate rotDown;
    private CameraRotate rotLeft;
    private CameraRotate rotRight;
    
    private Vector3f lookAtCenter = Vector3f.ZERO;
    
    private static final float rotationAmount = 1.0f/20.0f;
    private static final float rotationAmountNeg = -1*rotationAmount;
    
    private static Vector3f leftRightAxis = Vector3f.UNIT_Y;
    private static Vector3f upDownAxis = Vector3f.UNIT_X;
    
    private InputManager inputMang;
    
    public CameraTracker(Camera currentCam, FlyByCamera currentFlyCam, InputManager manager){
        this.currentCam = currentCam;
        this.currentFlyCam = currentFlyCam;
        this.inputMang = manager;
        addMovementListeners();
        addRadialMovementListeners();
        addRotationListeners();
        setDefaultCamera();
        enableFlyCam();
    }
    
    private void addRotationListeners(){
        rotateUp();
        rotateDown();
        rotateLeft();
        rotateRight();
    }
    
    private void addRotateListener(String name, int mouseCode, boolean dir, float rotAmount,Vector3f rotAxis){
        Matrix3f rotMatrix = GeneralHelper.getRotationMatrix(rotAmount, rotAxis);
        new CameraRotate(inputMang,name,mouseCode,dir,currentCam,rotMatrix,lookAtCenter);
    }
    private void rotateUp(){
        addRotateListener("rotCameraUp",MouseInput.AXIS_Y,false,rotationAmountNeg, upDownAxis);
    }
    private void rotateDown(){
        addRotateListener("rotCameraDown",MouseInput.AXIS_Y,true,rotationAmount, upDownAxis);
    }
    private void rotateLeft(){
        addRotateListener("rotCameraLeft",MouseInput.AXIS_X,false,rotationAmountNeg, leftRightAxis);
    }
    private void rotateRight(){
        addRotateListener("rotCameraRight",MouseInput.AXIS_X,true,rotationAmount, leftRightAxis);
    }
    
    private void addMovementListener(String name,int keyCode){
        new CameraMovement(inputMang,name,keyCode,currentCam);
    }
    private void moveUp(){
        addMovementListener("moveCameraUp",KeyInput.KEY_UP);
    }
    private void moveDown(){
        addMovementListener("moveCameraDown",KeyInput.KEY_DOWN);
    }
    private void moveLeft(){
        addMovementListener("moveCameraLeft",KeyInput.KEY_LEFT);
    }
    private void moveRight(){
        addMovementListener("moveCameraRight",KeyInput.KEY_RIGHT);
    }
    
    private void addMovementListeners(){
        moveUp(); 
        moveDown();
        moveLeft();
        moveRight();
    }
    
    private void addRadialMovementListeners(){
        moveInward();
        moveOutward();
    }
    private void addRadialMovementListener(String name, int keyCode, boolean inward){
        new CameraRadialMovement(inputMang,name,keyCode,currentCam,lookAtCenter,inward);
    }
    private void moveInward(){
        addRadialMovementListener("moveInward",KeyInput.KEY_R,true);
    }
    private void moveOutward(){
        addRadialMovementListener("moveOutward",KeyInput.KEY_F,false);
    }
    
    private void setDefaultCamera(){
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
    
    private void enableFlyCam(){
        currentFlyCam.setEnabled(true);
        currentFlyCam.setDragToRotate(true);
        currentFlyCam.setMoveSpeed(10f);
        currentFlyCam.setRotationSpeed(0f);
    }
    
}
