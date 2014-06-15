/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraImpl;

import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import mouseKeyboard.GeneralKeyboardActionMethod;

/**
 *
 * @author BLI
 */
public class CameraRadialMovement extends GeneralKeyboardActionMethod{
    
    private Camera camera;
    private String name;
    
    private Vector3f lookAtCenter;
    
    private float moveAmount = 1.0f/5.0f;
    
    public CameraRadialMovement(InputManager manager, String name, int keyCode, Camera camera, Vector3f lookAtCenter, boolean inward){
        super(manager,name,keyCode);
        this.camera = camera;
        this.name = name;
        this.lookAtCenter = lookAtCenter;
        if(inward){
            moveAmount = -1*moveAmount;
        }
    }

    @Override
    public void actionMethod() {
        Vector3f currentLoc = camera.getLocation();
        Vector3f direction = currentLoc.subtract(lookAtCenter);
        direction.normalizeLocal();
        Vector3f moveVector = direction.mult(moveAmount);
        camera.setLocation(currentLoc.add(moveVector));
    }
    
}
