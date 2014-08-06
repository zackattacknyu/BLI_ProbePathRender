/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.cameraTracker.cameraCoordIO;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.renderer.Camera;
import java.nio.file.Path;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;

/**
 *
 * @author BLI
 */
public class ImportCameraCoordinates extends GeneralKeyboardActionMethod{
    
    private Camera cam;
    private Path importPath;
    
    public ImportCameraCoordinates(InputManager inputManager,Camera cam, Path importPath){
        super(inputManager,"inputCameraCoords",KeyInput.KEY_U);
        this.cam = cam;
        this.importPath = importPath;
    }

    @Override
    public void actionMethod() {
        CameraCoordProperties.selectFileAndSetCameraCoords(cam, importPath.toFile());
    }
    
}
