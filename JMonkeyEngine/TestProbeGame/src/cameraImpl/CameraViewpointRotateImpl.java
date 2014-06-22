/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraImpl;

import camera.CameraHelper;
import camera.CameraViewpointRotate;
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
public class CameraViewpointRotateImpl extends GeneralKeyboardActionMethod implements CameraViewpointRotate{
    
    private Camera camera;
    private String name;
    
    private Vector3f rotationAxis;
    private float rotationAngle;
    
    public CameraViewpointRotateImpl(InputManager manager, String name, int keyCode, Camera camera, Vector3f rotAxis, float angle){
        super(manager,name,keyCode);
        this.camera = camera;
        this.name = name;
        this.rotationAxis = rotAxis;
        this.rotationAngle = angle;
    }

    @Override
    public void actionMethod() {
        rotateCameraViewpoint(rotationAxis,rotationAngle);        
    }


    public void rotateCameraViewpoint(Vector3f rotAxis, float rotAngle) {
        camera.setAxes(CameraHelper.getRotationAxes(
                rotAngle, rotAxis, 
                camera.getUp(), 
                camera.getLeft(), 
                camera.getDirection()));
    }
    
}
