/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package camera;

import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
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
    
    public CameraTracker(Camera currentCam, FlyByCamera currentFlyCam, InputManager manager){
        this.currentCam = currentCam;
        this.currentFlyCam = currentFlyCam;
        addMovementListeners(manager);
        addRotationListeners(manager);
        setDefaultCamera();
        enableFlyCam();
    }
    
    private void addRotationListeners(InputManager manager){
        Matrix3f rotLeftMatrix = GeneralHelper.getRotationMatrix(-1.0f/20.0f, Vector3f.UNIT_Y);
        Matrix3f rotRightMatrix = GeneralHelper.getRotationMatrix(1.0f/20.0f, Vector3f.UNIT_Y);
        Matrix3f rotUpMatrix = GeneralHelper.getRotationMatrix(-1.0f/20.0f, Vector3f.UNIT_X);
        Matrix3f rotDownMatrix = GeneralHelper.getRotationMatrix(1.0f/20.0f, Vector3f.UNIT_X);
        
        rotUp = new CameraRotate(manager,"rotCameraUp",MouseInput.AXIS_Y,false,currentCam,rotUpMatrix,lookAtCenter);
        rotDown = new CameraRotate(manager,"rotCameraDown",MouseInput.AXIS_Y,true,currentCam,rotDownMatrix,lookAtCenter);
        rotLeft = new CameraRotate(manager,"rotCameraLeft",MouseInput.AXIS_X,false,currentCam,rotLeftMatrix,lookAtCenter);
        rotRight = new CameraRotate(manager,"rotCameraRight",MouseInput.AXIS_X,true,currentCam,rotRightMatrix,lookAtCenter);
    }
    
    private void addMovementListeners(InputManager manager){
        CameraMovement moveUp = new CameraMovement(manager,"moveCameraUp",
                KeyInput.KEY_UP,currentCam);
        CameraMovement moveDown = new CameraMovement(manager,"moveCameraDown",
                KeyInput.KEY_DOWN,currentCam);
        CameraMovement moveLeft = new CameraMovement(manager,"moveCameraLeft",
                KeyInput.KEY_LEFT,currentCam);
        CameraMovement moveRight = new CameraMovement(manager,"moveCameraRight",
                KeyInput.KEY_RIGHT,currentCam);
       
        CameraRadialMovement moveInward = new CameraRadialMovement(
                manager,"moveInward",KeyInput.KEY_R,currentCam,lookAtCenter,true);
        CameraRadialMovement moveOutward = new CameraRadialMovement(
                manager,"moveOutward",KeyInput.KEY_F,currentCam,lookAtCenter,false);
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
