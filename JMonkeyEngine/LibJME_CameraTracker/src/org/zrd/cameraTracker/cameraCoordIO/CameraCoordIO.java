/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.cameraTracker.cameraCoordIO;

import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import java.nio.file.Path;

/**
 *
 * @author BLI
 */
public class CameraCoordIO {
    
    public CameraCoordIO(InputManager inputManager, Camera cam, Path dataPath){
        new OutputCameraCoordinates(inputManager,cam,dataPath);
        new ImportCameraCoordinates(inputManager,cam,dataPath);
    }
    
}
