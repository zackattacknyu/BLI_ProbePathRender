/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraImpl_ProbePathRender;

import cameraImpl.CameraTrackerImpl;
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
    
    public static final Vector3f SPHERE_MODE_DEFAULT_LOCATION = 
            new Vector3f(-22.649244f, -17.260416f, -67.74668f);
    public static final Quaternion SPHERE_MODE_DEFAULT_ROTATION = 
            new Quaternion(0.17899777f, 0.0838113f, 0.95806247f, -0.20748913f);
    
    public static final Vector3f LOLA_MODE_DEFAULT_LOCATION = 
            new Vector3f(-16.928802f, 23.251862f, -54.489124f);
    public static final Quaternion LOLA_MODE_DEFAULT_ROTATION = 
            new Quaternion(0.20308718f, 0.20007013f, -0.042432234f, 0.9575631f);
    
    public static final Vector3f RAW_DATA_MODE_DEFAULT_LOCATION = 
            new Vector3f(-0.67807275f, 0.5436802f, -2.7487648f);
    public static final Quaternion RAW_DATA_MODE_DEFAULT_ROTATION = 
            new Quaternion(0.15252174f, -2.9824104E-4f, 0.7722618f, 0.61672425f);
    
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
                currentCam.setLocation(SPHERE_MODE_DEFAULT_LOCATION);
                currentCam.setRotation(SPHERE_MODE_DEFAULT_ROTATION);
                break;
                
            case 1:
                //settings for when viewing lola
                currentCam.setLocation(LOLA_MODE_DEFAULT_LOCATION);
                currentCam.setRotation(LOLA_MODE_DEFAULT_ROTATION);
                break;
                
            case 2:
                //when viewing raw data
                currentCam.setLocation(RAW_DATA_MODE_DEFAULT_LOCATION);
                currentCam.setRotation(RAW_DATA_MODE_DEFAULT_ROTATION);
                break;
        }
    }
    
    
    
}
