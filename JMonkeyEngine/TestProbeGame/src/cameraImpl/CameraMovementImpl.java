/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraImpl;

import camera.CameraHelper;
import camera.CameraMovement;
import camera.CameraRotate;
import com.jme3.input.InputManager;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import mouseKeyboard.GeneralKeyboardActionMethod;
import util.AngleAxisRotation;

/**
 *
 * @author BLI
 */
public class CameraMovementImpl extends GeneralKeyboardActionMethod implements CameraMovement{
    
    private Camera camera;
    private String name;
    
    public CameraMovementImpl(InputManager manager, String name, int keyCode, Camera camera){
        super(manager,name,keyCode);
        this.camera = camera;
        this.name = name;
    }

    @Override
    public void actionMethod() {
        Vector3f rotAxis = camera.getLeft();
        float angle = CameraRotate.ROTATION_AMOUNT;
        camera.setAxes(CameraHelper.getRotationAxes(
                angle, rotAxis, 
                camera.getUp(), 
                camera.getLeft(), 
                camera.getDirection()));
    }

    public void moveCameraUp() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void moveCameraDown() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void moveCameraLeft() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void moveCameraRight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void moveCamera() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
