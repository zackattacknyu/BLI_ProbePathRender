/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.rawProbeDataDisplay;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import java.io.File;
import java.nio.file.Path;
import java.util.Properties;
import org.zrd.util.fileHelper.GeneralFileHelper;
import org.zrd.util.properties.PropertiesHelper;

/**
 *
 * @author BLI
 */
public class CameraCoordProperties {
    
    public static final String CAMERA_LOCATION_PROPERTY_NAMES_PREFIX = 
            "camLocation.";
    
    public static final String CAMERA_LOCATION_X_PROPERTY_NAME = 
            CAMERA_LOCATION_PROPERTY_NAMES_PREFIX + "x";
    public static final String CAMERA_LOCATION_Y_PROPERTY_NAME = 
            CAMERA_LOCATION_PROPERTY_NAMES_PREFIX + "y";
    public static final String CAMERA_LOCATION_Z_PROPERTY_NAME = 
            CAMERA_LOCATION_PROPERTY_NAMES_PREFIX + "z";
    
    public static final String CAMERA_ROTATION_PROPERTY_NAMES_PREFIX = 
            "camRotation.";
    
    public static final String CAMERA_ROTATION_W_PROPERTY_NAME = 
            CAMERA_ROTATION_PROPERTY_NAMES_PREFIX + "w";
    public static final String CAMERA_ROTATION_X_PROPERTY_NAME = 
            CAMERA_ROTATION_PROPERTY_NAMES_PREFIX + "x";
    public static final String CAMERA_ROTATION_Y_PROPERTY_NAME = 
            CAMERA_ROTATION_PROPERTY_NAMES_PREFIX + "y";
    public static final String CAMERA_ROTATION_Z_PROPERTY_NAME = 
            CAMERA_ROTATION_PROPERTY_NAMES_PREFIX + "z";
    
    
    private Properties camCoords;
    
    public CameraCoordProperties(Properties camCoords){
        this.camCoords = camCoords;
    }
    
    public CameraCoordProperties(File camCoordsFile){
        camCoords = PropertiesHelper.getProperties(camCoordsFile);
    }
    
    public CameraCoordProperties(Camera cam){
        this(cam.getLocation(),cam.getRotation());
    }
    
    public static void writeCameraCoordinatesToFile(Camera cam, Path dataFolder){
        CameraCoordProperties camCoordProps = new CameraCoordProperties(cam);
        camCoordProps.writeCameraCoordProperties(dataFolder);
    }
    
    public static void selectFileAndSetCameraCoords(Camera cam, File initDirectory){
        File cameraCoordFile = GeneralFileHelper.importPathUsingFileSelector(initDirectory);
        if(cameraCoordFile != null) setCameraCoordinatesUsingFile(cam,cameraCoordFile);
    }
    
    public static void setCameraCoordinatesUsingFile(Camera cam, File cameraCoordFile){
        CameraCoordProperties cameraCoords = new CameraCoordProperties(cameraCoordFile);
        cam.setLocation(cameraCoords.getCamLocation());
        cam.setRotation(cameraCoords.getCamRotation());
    }
    public CameraCoordProperties(Vector3f location, Quaternion rotation){
        camCoords = new Properties();
        setCamLocationProperties(location);
        setCamRotationProperties(rotation);
    }
    
    public void writeCameraCoordProperties(Path folder){
        PropertiesHelper.writePropertiesFile(camCoords, folder, 
                "cameraCoords_","---Camera Location and Rotation Information---");
    }
    
    private void setCamCoordProperty(String name, String value){
        camCoords.setProperty(name, value);
    }
    
    private void setCamCoordProperty(String name, float value){
        setCamCoordProperty(name,String.valueOf(value));
    }
    
    private void setCamLocationProperties(Vector3f location){
        setCamCoordProperty(CAMERA_LOCATION_X_PROPERTY_NAME,location.getX());
        setCamCoordProperty(CAMERA_LOCATION_Y_PROPERTY_NAME,location.getY());
        setCamCoordProperty(CAMERA_LOCATION_Z_PROPERTY_NAME,location.getZ());
    }
    
    private void setCamRotationProperties(Quaternion rotation){
        setCamCoordProperty(CAMERA_ROTATION_W_PROPERTY_NAME,rotation.getW());
        setCamCoordProperty(CAMERA_ROTATION_X_PROPERTY_NAME,rotation.getX());
        setCamCoordProperty(CAMERA_ROTATION_Y_PROPERTY_NAME,rotation.getY());
        setCamCoordProperty(CAMERA_ROTATION_Z_PROPERTY_NAME,rotation.getZ());
    }
    
    public Vector3f getCamLocation(){
        float xVal = getFloatProperty(CameraCoordProperties.CAMERA_LOCATION_X_PROPERTY_NAME);
        float yVal = getFloatProperty(CameraCoordProperties.CAMERA_LOCATION_Y_PROPERTY_NAME);
        float zVal = getFloatProperty(CameraCoordProperties.CAMERA_LOCATION_Z_PROPERTY_NAME);
        
        return new Vector3f(xVal,yVal,zVal);
    }
    
    public Quaternion getCamRotation(){
        float wVal = getFloatProperty(CameraCoordProperties.CAMERA_ROTATION_W_PROPERTY_NAME);
        float xVal = getFloatProperty(CameraCoordProperties.CAMERA_ROTATION_X_PROPERTY_NAME);
        float yVal = getFloatProperty(CameraCoordProperties.CAMERA_ROTATION_Y_PROPERTY_NAME);
        float zVal = getFloatProperty(CameraCoordProperties.CAMERA_ROTATION_Z_PROPERTY_NAME);
        return new Quaternion(wVal,xVal,yVal,zVal);
    }
    
    public float getFloatProperty(String propertyName){
        return PropertiesHelper.getFloatValueProperty(camCoords,propertyName);
    }
    
}
