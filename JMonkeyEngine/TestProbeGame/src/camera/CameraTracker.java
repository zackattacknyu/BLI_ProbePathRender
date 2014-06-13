/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package camera;

import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 *
 * @author BLI
 */
public class CameraTracker {
    
    private Camera currentCam;
    private FlyByCamera currentFlyCam;
    
    public CameraTracker(Camera currentCam, FlyByCamera currentFlyCam){
        this.currentCam = currentCam;
        this.currentFlyCam = currentFlyCam;
    }
    
    public void addMovementListeners(InputManager manager){
        CameraMovement moveUp = new CameraMovement(manager,"moveCameraUp",
                KeyInput.KEY_UP,
                "Move Camera Up from current location of " + 
                currentCam.getLocation().toString());
        CameraMovement moveDown = new CameraMovement(manager,"moveCameraDown",
                KeyInput.KEY_DOWN,
                "Move Camera Down from current location of " + 
                currentCam.getLocation().toString());
        CameraMovement moveLeft = new CameraMovement(manager,"moveCameraLeft",
                KeyInput.KEY_LEFT,
                "Move Camera Left from current location of " + 
                currentCam.getLocation().toString());
        CameraMovement moveRight = new CameraMovement(manager,"moveCameraRight",
                KeyInput.KEY_RIGHT,
                "Move Camera Right from current location of " + 
                currentCam.getLocation().toString());
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
    
    public void enableFlyCam(){
        currentFlyCam.setEnabled(true);
        currentFlyCam.setDragToRotate(true);
        currentFlyCam.setMoveSpeed(10f);
        currentFlyCam.setRotationSpeed(0f);
    }
    
}
