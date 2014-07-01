/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.cameraTracker.cameraMoveImpl;

import org.zrd.renderUtil.mouseKeyboard.MouseClickImpl;
import org.zrd.cameraTracker.cameraMoves.CameraHelper;
import org.zrd.cameraTracker.cameraMoves.CameraRotate;
import com.jme3.input.InputManager;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import org.zrd.renderUtil.mouseKeyboard.GeneralMouseAnalogMethod;

/**This rotates the whole camera about a center point
 *  and ensures that it is still looking at that center point
 *
 * @author BLI
 */
public class CameraRotateImpl extends GeneralMouseAnalogMethod implements CameraRotate{
    
    private Camera camera;
    private Matrix3f rotMatrix;
    private Vector3f lookAtCenter;
    private MouseClickImpl mouseClick;
    
    /**
     * This constructs the rotate listener
     * @param manager           current input manager
     * @param name              name of rotation action. should be unique
     * @param mouseCode         code for the mouse click action
     * @param negative          whether or not mouse axis movement is down or up
     * @param camera            current camera object
     * @param rotMatrix         rotation matrix representing how to rotate camera
     * @param lookAtCenter      point to ensure it is still looking at and point to rotate by
     */
    public CameraRotateImpl(InputManager manager, String name, int mouseCode, boolean negative, 
            Camera camera, Matrix3f rotMatrix, Vector3f lookAtCenter){
        super(manager,name,mouseCode,negative);
        this.camera = camera;
        this.rotMatrix = rotMatrix;
        this.lookAtCenter = lookAtCenter;
        
        mouseClick = new MouseClickImpl(manager);
    }

    /**
     * This is called whenever the mouse is being moved at all.
     *      It ensures that it is being pressed down and then
     *      calls the methods that rotate the camera if it is being pressed
     *      down.
     */
    @Override
    public void analogMethod() {
        if(shouldRotate()){
            changeLocation();
            changeLookAt();
        }
    }

    /**
     * Tells whether the rotation should occur, which boils down to whether
     *      the mouse is currently being clicked
     * @return      whether the mouse is pressed down
     */
    public boolean shouldRotate() {
        return mouseClick.isMouseDown();
    }

    /**
     * Changes the camera location by rotating it about a center point
     */
    public void changeLocation() {
        camera.setLocation(CameraHelper.getRotatedCameraLocation(
                    rotMatrix, camera.getLocation()));
    }

    /**
     * Changes the camera's lookAt position to the center point
     *      after the rotation is done
     */
    public void changeLookAt() {
        camera.lookAt(CameraHelper.getLookAtCenter(lookAtCenter), 
                    CameraHelper.getLookAtUpVector(camera.getUp()));
    }
    
}
