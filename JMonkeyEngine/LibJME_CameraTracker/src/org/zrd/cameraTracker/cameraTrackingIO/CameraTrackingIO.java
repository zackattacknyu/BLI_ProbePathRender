/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.cameraTracker.cameraTrackingIO;

import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import org.zrd.cameraTracker.cameraCoordIO.CameraCoordIO;
import org.zrd.cameraTracker.cameraMoveImpl.CameraTrackerImpl;
import org.zrd.util.fileHelper.FilePathHelper;
import org.zrd.util.fileHelper.MeshInteractionFiles;

/**
 *
 * @author Zach
 */
public class CameraTrackingIO {
    
    public static void initializeCameraTrackingIO(InputManager inputManager, Camera cam, FlyByCamera flyCam){
        initializeCameraTrackingIO(inputManager,cam,flyCam,null);
    }
    
    public static void initializeCameraTrackingIO(InputManager inputManager, 
            Camera cam, FlyByCamera flyCam, MeshInteractionFiles meshInterFiles){
        new CameraTrackerImpl(cam,flyCam,inputManager);
        new CameraCoordIO(inputManager,cam,FilePathHelper.getDefaultOutputFolder(),meshInterFiles);
    }
}
