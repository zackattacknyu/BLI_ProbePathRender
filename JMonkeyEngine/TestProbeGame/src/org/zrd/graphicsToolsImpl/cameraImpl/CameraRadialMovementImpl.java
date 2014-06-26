/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.graphicsToolsImpl.cameraImpl;

import org.zrd.graphicsTools.camera.CameraHelper;
import org.zrd.graphicsTools.camera.CameraRadialMovement;
import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import org.zrd.utilImpl.mouseKeyboard.GeneralKeyboardActionMethod;

/**
 * This is used to implement camera radial movment in
 *      JMonkeyEngine
 *
 * @author BLI
 */
public class CameraRadialMovementImpl extends GeneralKeyboardActionMethod implements CameraRadialMovement{
    
    private Camera camera;
    private String name;
    private boolean inward;
    private Vector3f lookAtCenter;
    
    /**
     * Instantiates the camera radial movement
     * @param manager       input manager being used currently
     * @param name          name of movement. should be unique
     * @param keyCode       key code of keyboard key to use
     * @param camera        current camera object
     * @param lookAtCenter  the current look at center
     * @param inward        whether or not the radial movement is inward
     */
    public CameraRadialMovementImpl(InputManager manager, String name, int keyCode, Camera camera, Vector3f lookAtCenter, boolean inward){
        super(manager,name,keyCode);
        this.camera = camera;
        this.name = name;
        this.inward = inward;
        this.lookAtCenter = lookAtCenter;
    }

    /**
     * This actually moves the camera. It is called when one of the mapped
     *      keys is pressed down.
     */
    @Override
    public void actionMethod() {
        if(isInward()){
            move(MOVE_AMOUNT_INWARD);
        }else{
            move(MOVE_AMOUNT_OUTWARD);
        }
    }

    /**
     * Tells whether the movement is inward or outward
     * @return  if movement to be done is inward
     */
    public boolean isInward() {
        return inward;
    }

    /**
     * This does the required operations to move the camera
     * @param amount    the amount to move the camera
     */
    public void move(float amount) {
        camera.setLocation(
                CameraHelper.getNewRadialLocation(
                camera.getLocation(), lookAtCenter, amount));
    }


    
}
