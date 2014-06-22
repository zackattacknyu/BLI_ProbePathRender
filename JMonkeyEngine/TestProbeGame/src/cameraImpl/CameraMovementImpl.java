/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraImpl;

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
        Matrix3f mat = new Matrix3f();
        
        /**
         * This code is lifted directly from the flyCamera rotate code
         */
        mat.fromAngleNormalAxis(CameraRotate.ROTATION_AMOUNT, camera.getLeft());

        Vector3f up = camera.getUp();
        Vector3f left = camera.getLeft();
        Vector3f dir = camera.getDirection();

        mat.mult(up, up);
        mat.mult(left, left);
        mat.mult(dir, dir);

        Quaternion q = new Quaternion();
        q.fromAxes(left, up, dir);
        q.normalizeLocal();

        camera.setAxes(q);
        System.out.println(name + " from Location of " + camera.getLocation().toString());
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
