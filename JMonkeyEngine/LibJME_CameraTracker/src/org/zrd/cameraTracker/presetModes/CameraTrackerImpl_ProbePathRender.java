/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.cameraTracker.presetModes;

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
public class CameraTrackerImpl_ProbePathRender extends CameraTrackerImpl{
    
    //default camera when viewing the sphere
    public static final Vector3f SPHERE_MODE_DEFAULT_LOCATION = 
            new Vector3f(-22.649244f, -17.260416f, -67.74668f);
    public static final Quaternion SPHERE_MODE_DEFAULT_ROTATION = 
            new Quaternion(0.17899777f, 0.0838113f, 0.95806247f, -0.20748913f);
    
    //default camera when viewing lola, the most common mode
    public static final Vector3f LOLA_MODE_DEFAULT_LOCATION = 
            new Vector3f(-16.928802f, 23.251862f, -54.489124f);
    public static final Quaternion LOLA_MODE_DEFAULT_ROTATION = 
            new Quaternion(0.20308718f, 0.20007013f, -0.042432234f, 0.9575631f);

    //default camera when viewing ball
    public static final Vector3f BALL_MODE_DEFAULT_LOCATION = 
            new Vector3f(9.057041f, 0.40602827f, 10.088496f);
    public static final Quaternion BALL_MODE_DEFAULT_ROTATION = 
            new Quaternion(-0.28912917f, -0.122892424f, 0.91898036f, 0.23827894f);
    
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
    public CameraTrackerImpl_ProbePathRender(Camera currentCam, FlyByCamera currentFlyCam, InputManager manager){
        super(currentCam,currentFlyCam,manager);
    }

    @Override
    public void setDefaultCamera() {
        setDefaultCamera((short)1); 
    }

    @Override
    public void setDefaultCamera(short mode) {
        switch(mode){
            case 0:
                //when viewing the sphere
                setDefaultCamera(SPHERE_MODE_DEFAULT_LOCATION,SPHERE_MODE_DEFAULT_ROTATION);
                break;
                
            case 1:
                //settings for when viewing lola
                setDefaultCamera(LOLA_MODE_DEFAULT_LOCATION,LOLA_MODE_DEFAULT_ROTATION);
                break;
                
            case 2:
                //when viewing raw data
                setDefaultCamera(RAW_DATA_MODE_DEFAULT_LOCATION,RAW_DATA_MODE_DEFAULT_ROTATION);
                break;
                
            case 3:
                //when viewing ball
                setDefaultCamera(BALL_MODE_DEFAULT_LOCATION,BALL_MODE_DEFAULT_ROTATION);
        }
    }
    
    
    
}
