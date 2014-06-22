/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraImpl;

import camera.CameraHelper;
import camera.CameraRadialMovement;
import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import mouseKeyboard.GeneralKeyboardActionMethod;

/**
 *
 * @author BLI
 */
public class CameraRadialMovementImpl extends GeneralKeyboardActionMethod implements CameraRadialMovement{
    
    private Camera camera;
    private String name;
    
    private boolean inward;
    
    private Vector3f lookAtCenter;
    
    public CameraRadialMovementImpl(InputManager manager, String name, int keyCode, Camera camera, Vector3f lookAtCenter, boolean inward){
        super(manager,name,keyCode);
        this.camera = camera;
        this.name = name;
        this.inward = inward;
        this.lookAtCenter = lookAtCenter;
    }

    @Override
    public void actionMethod() {
        if(isInward()){
            move(MOVE_AMOUNT_INWARD);
        }else{
            move(MOVE_AMOUNT_OUTWARD);
        }
    }

    public boolean isInward() {
        return inward;
    }

    public void move(float amount) {
        camera.setLocation(
                CameraHelper.getNewRadialLocation(
                camera.getLocation(), lookAtCenter, amount));
    }


    
}
