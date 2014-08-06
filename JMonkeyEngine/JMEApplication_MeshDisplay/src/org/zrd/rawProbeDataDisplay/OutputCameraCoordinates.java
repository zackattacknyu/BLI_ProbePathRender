/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.rawProbeDataDisplay;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.renderer.Camera;
import java.nio.file.Path;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;

/**
 *
 * @author BLI
 */
public class OutputCameraCoordinates extends GeneralKeyboardActionMethod{
    
    private Camera cam;
    private Path dataPath;
    
    public OutputCameraCoordinates(InputManager inputManager,Camera cam, Path dataPath){
        super(inputManager,"outputCameraCoords",KeyInput.KEY_O);
        this.cam = cam;
        this.dataPath = dataPath;
    }

    @Override
    public void actionMethod() {
        System.out.println("Now writing camera coordinates");
        CameraCoordProperties.writeCameraCoordinatesToFile(cam, dataPath);
        System.out.println("Now finished writing camera coordinates");
    }
    
}
