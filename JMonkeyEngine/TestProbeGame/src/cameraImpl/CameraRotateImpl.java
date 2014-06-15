/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraImpl;

import com.jme3.input.InputManager;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import mouseKeyboard.GeneralMouseAnalogMethod;

/**
 *
 * @author BLI
 */
public class CameraRotateImpl extends GeneralMouseAnalogMethod{
    
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
        if(mouseClick.isMouseDown()){
            camera.setLocation(rotMatrix.mult(camera.getLocation()));
            camera.lookAt(lookAtCenter, camera.getUp());
        }
    }
    
}
