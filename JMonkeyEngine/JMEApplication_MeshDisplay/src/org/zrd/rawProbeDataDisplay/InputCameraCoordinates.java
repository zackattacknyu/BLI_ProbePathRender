/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.rawProbeDataDisplay;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;
import org.zrd.util.dataWriting.ProbeDataWriter;
import org.zrd.util.fileHelper.GeneralFileHelper;
import org.zrd.util.properties.PropertiesHelper;

/**
 *
 * @author BLI
 */
public class InputCameraCoordinates extends GeneralKeyboardActionMethod{
    
    private Camera cam;
    private Path importPath;
    
    public InputCameraCoordinates(InputManager inputManager,Camera cam, Path importPath){
        super(inputManager,"inputCameraCoords",KeyInput.KEY_U);
        this.cam = cam;
        this.importPath = importPath;
    }

    @Override
    public void actionMethod() {
        File cameraCoordFile = GeneralFileHelper.importPathUsingFileSelector(importPath.toFile());
        Properties cameraCoords = PropertiesHelper.getProperties(cameraCoordFile);
        cam.setLocation(getCamLocationFromProperties(cameraCoords));
        cam.setRotation(getCamRotationFromProperties(cameraCoords));
    }
    
    public static Vector3f getCamLocationFromProperties(Properties camCoords){
        String camLocPrefixPropName = "camLocation.";
        String xVal = camCoords.getProperty(camLocPrefixPropName + "x");
        String yVal = camCoords.getProperty(camLocPrefixPropName + "y");
        String zVal = camCoords.getProperty(camLocPrefixPropName + "z");
        return new Vector3f(
                Float.valueOf(xVal),
                Float.valueOf(yVal),
                Float.valueOf(zVal));
    }
    
    public static Quaternion getCamRotationFromProperties(Properties camCoords){
        String camRotPrefixPropName = "camRotation.";
        String wVal = camCoords.getProperty(camRotPrefixPropName + "w");
        String xVal = camCoords.getProperty(camRotPrefixPropName + "x");
        String yVal = camCoords.getProperty(camRotPrefixPropName + "y");
        String zVal = camCoords.getProperty(camRotPrefixPropName + "z");
        return new Quaternion(
                Float.valueOf(wVal),
                Float.valueOf(xVal),
                Float.valueOf(yVal),
                Float.valueOf(zVal));
    }
    
}
