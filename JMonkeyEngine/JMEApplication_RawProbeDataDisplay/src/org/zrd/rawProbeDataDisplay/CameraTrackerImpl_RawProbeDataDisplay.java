/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.rawProbeDataDisplay;

import org.zrd.cameraTracker.cameraMoveImpl.CameraTrackerImpl;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 *
 * @author BLI
 */
public class CameraTrackerImpl_RawProbeDataDisplay extends CameraTrackerImpl{
    
    //default camera when viewing raw data
    public static final Vector3f RAW_DATA_MODE_DEFAULT_LOCATION = 
            new Vector3f(-0.67807275f, 0.5436802f, -2.7487648f);
    public static final Quaternion RAW_DATA_MODE_DEFAULT_ROTATION = 
            new Quaternion(0.15252174f, -2.9824104E-4f, 0.7722618f, 0.61672425f);
    
    /**
     * Initializes the camera for the PathProbeRender application.
     *      This includes methods that set its default camera
     * @param currentCam            camera object
     * @param currentFlyCam         fly camera object
     * @param manager               input manager
     */
    public CameraTrackerImpl_RawProbeDataDisplay(Camera currentCam, FlyByCamera currentFlyCam, InputManager manager){
        super(currentCam,currentFlyCam,manager);
    }

    @Override
    public void setDefaultCamera() {
        //when viewing raw data
        setDefaultCamera(RAW_DATA_MODE_DEFAULT_LOCATION,RAW_DATA_MODE_DEFAULT_ROTATION);
    }

    @Override
    public void setDefaultCamera(short mode) {
        setDefaultCamera();
    }
    
    
    
}
