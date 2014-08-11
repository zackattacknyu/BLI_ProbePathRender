/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.cameraTracker.cameraCoordIO;

import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import java.nio.file.Path;
import org.zrd.util.fileHelper.MeshInteractionFiles;

/**
 *
 * @author BLI
 */
public class CameraCoordIO {
    
    
    
    
    public CameraCoordIO(InputManager inputManager, Camera cam, Path dataPath,MeshInteractionFiles meshInterFiles){
        new OutputCameraCoordinates(inputManager,cam,dataPath,meshInterFiles);
        new ImportCameraCoordinates(inputManager,cam,dataPath);
    }
    public CameraCoordIO(InputManager inputManager, Camera cam, Path dataPath){
        this(inputManager,cam,dataPath,null);
    }
}
