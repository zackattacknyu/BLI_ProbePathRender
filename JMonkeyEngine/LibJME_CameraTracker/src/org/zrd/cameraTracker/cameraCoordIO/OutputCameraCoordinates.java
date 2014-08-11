/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.cameraTracker.cameraCoordIO;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.renderer.Camera;
import java.io.File;
import java.nio.file.Path;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;
import org.zrd.util.fileHelper.MeshInteractionFiles;

/**
 *
 * @author BLI
 */
public class OutputCameraCoordinates extends GeneralKeyboardActionMethod{
    
    private Camera cam;
    private Path dataPath;
    private MeshInteractionFiles meshInterFiles;
    
    public OutputCameraCoordinates(InputManager inputManager,Camera cam, Path dataPath){
        super(inputManager,"outputCameraCoords",KeyInput.KEY_O);
        this.cam = cam;
        this.dataPath = dataPath;
        this.meshInterFiles = null;
    }
    
    public OutputCameraCoordinates(InputManager inputManager,Camera cam, Path dataPath, MeshInteractionFiles meshInterFiles){
        this(inputManager,cam,dataPath);
        this.meshInterFiles = meshInterFiles;
    }

    @Override
    public void actionMethod() {
        System.out.println("Now writing camera coordinates");
        File camCoordFile = CameraCoordProperties.writeCameraCoordinatesToFile(cam, dataPath);
        if(meshInterFiles != null) meshInterFiles.setCameraCoordFileToCopy(camCoordFile);
        System.out.println("Now finished writing camera coordinates");
    }
    
}
