/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.graphicsToolsImpl.cameraImpl;

import org.zrd.graphicsTools.camera.CameraHelper;
import org.zrd.graphicsTools.camera.CameraViewpointRotate;
import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import org.zrd.renderUtil.mouseKeyboard.GeneralKeyboardActionMethod;

/**This is used to implement rotating the camera viewpoint 
 *      in JMonkeyEngine
 *
 * @author BLI
 */
public class CameraViewpointRotateImpl extends GeneralKeyboardActionMethod implements CameraViewpointRotate{
    
    private Camera camera;
    private String name;
    private Vector3f rotationAxis;
    private float rotationAngle;
    
    /**
     * This instantiates the listener for rotating the camera viewpoint
     * @param manager       the current input manager
     * @param name          the name of the listener. should be unique
     * @param keyCode       the keycode of the key to be mapped to an action
     * @param camera        the current camera object
     * @param rotAxis       the axis to rotate the viewpoint by
     * @param angle         the angle to rotate the viewpoint
     */
    public CameraViewpointRotateImpl(InputManager manager, String name, int keyCode, Camera camera, Vector3f rotAxis, float angle){
        super(manager,name,keyCode);
        this.camera = camera;
        this.name = name;
        this.rotationAxis = rotAxis;
        this.rotationAngle = angle;
    }

    /**
     * This is called whenever the key is pressed and it calls the method
     *      that rotates the camera viewpoint
     */
    @Override
    public void actionMethod() {
        rotateCameraViewpoint(rotationAxis,rotationAngle);        
    }

    /**
     * This roates the camera viewpoint
     * @param rotAxis       axis to rotate by
     * @param rotAngle      angle of rotation
     */
    public void rotateCameraViewpoint(Vector3f rotAxis, float rotAngle) {
        camera.setAxes(CameraHelper.getRotationAxes(
                rotAngle, rotAxis, 
                camera.getUp(), 
                camera.getLeft(), 
                camera.getDirection()));
    }
    
}
