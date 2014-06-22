/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraImpl;

import camera.CameraHelper;
import camera.CameraRotate;
import com.jme3.input.InputManager;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import mouseKeyboard.GeneralMouseAnalogMethod;

/**
 *
 * @author BLI
 */
public class CameraRotateImpl extends GeneralMouseAnalogMethod implements CameraRotate{
    
    private Camera camera;
    private Matrix3f rotMatrix;
    private Vector3f lookAtCenter;
    private CameraMouseClickImpl mouseClick;
    
    public CameraRotateImpl(InputManager manager, String name, int mouseCode, boolean negative, 
            Camera camera, Matrix3f rotMatrix, Vector3f lookAtCenter){
        super(manager,name,mouseCode,negative);
        this.camera = camera;
        this.rotMatrix = rotMatrix;
        this.lookAtCenter = lookAtCenter;
        
        mouseClick = new CameraMouseClickImpl(manager);
    }

    @Override
    public void analogMethod() {
        if(shouldRotate()){
            changeLocation();
            changeLookAt();
        }
    }

    public boolean shouldRotate() {
        return mouseClick.isMouseDown();
    }

    public void changeLocation() {
        camera.setLocation(CameraHelper.getRotatedCameraLocation(
                    rotMatrix, camera.getLocation()));
    }

    public void changeLookAt() {
        camera.lookAt(CameraHelper.getLookAtCenter(lookAtCenter), 
                    CameraHelper.getLookAtUpVector(camera.getUp()));
    }
    
}
