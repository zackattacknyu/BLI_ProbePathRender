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
import java.util.Properties;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;

/**
 *
 * @author BLI
 */
public class OutputCameraCoordinates extends GeneralKeyboardActionMethod{
    
    private Camera cam;
    private File dataPath;
    
    public OutputCameraCoordinates(InputManager inputManager,Camera cam, File dataPath){
        super(inputManager,"outputCameraCoords",KeyInput.KEY_O);
        this.cam = cam;
        this.dataPath = dataPath;
    }

    @Override
    public void actionMethod() {
        Properties coords = getCameraCoords(cam);
    }
    
    public static Properties getCameraCoords(Camera cam){
        Properties cameraCoords = new Properties();
        
        Vector3f cameraLocation = cam.getLocation();
        String camLocPrefixPropName = "camLocation.";
        cameraCoords.setProperty(camLocPrefixPropName + "x", 
                String.valueOf(cameraLocation.getX()));
        cameraCoords.setProperty(camLocPrefixPropName + "y", 
                String.valueOf(cameraLocation.getY()));
        cameraCoords.setProperty(camLocPrefixPropName + "z", 
                String.valueOf(cameraLocation.getZ()));
        
        Quaternion cameraRotation = cam.getRotation();
        String camRotPrefixPropName = "camRotation.";
        cameraCoords.setProperty(camRotPrefixPropName + "w", 
                String.valueOf(cameraRotation.getW()));
        cameraCoords.setProperty(camRotPrefixPropName + "x", 
                String.valueOf(cameraRotation.getX()));
        cameraCoords.setProperty(camRotPrefixPropName + "y", 
                String.valueOf(cameraRotation.getY()));
        cameraCoords.setProperty(camRotPrefixPropName + "z", 
                String.valueOf(cameraRotation.getZ()));
        
        return cameraCoords;
    }
    
}
