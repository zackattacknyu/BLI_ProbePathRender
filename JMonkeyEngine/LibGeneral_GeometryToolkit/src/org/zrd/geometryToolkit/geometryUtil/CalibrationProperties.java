/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.geometryUtil;

import com.jme3.math.Quaternion;
import java.util.ArrayList;
import java.util.Properties;
import org.zrd.util.dataHelp.DisplacementHelper;
import org.zrd.util.properties.PropertiesHelper;

/**
 *
 * @author BLI
 */
public class CalibrationProperties {
    
    private Quaternion rotationCalib;
    private Float probeToRealFactor;
    private Float realToVirtualFactor;
    private Float scaleFactor;
    private Float scaleFactorX;
    private Float scaleFactorY;
    private boolean reflectX;
    private boolean reflectY;
    
    public static final String PROBE_TO_REAL_FACTOR_NAME = "scaleFactor.probeToReal";
    public static final String REAL_TO_VIRTUAL_FACTOR_NAME = "scaleFactor.realToVirtual";
    
    public static final String QUATERNION_W_FACTOR_NAME = "initQuat.w";
    public static final String QUATERNION_X_FACTOR_NAME = "initQuat.x";
    public static final String QUATERNION_Y_FACTOR_NAME = "initQuat.y";
    public static final String QUATERNION_Z_FACTOR_NAME = "initQuat.z";
    
    public static final String REFLECT_X_FACTOR_NAME = "reflectionCalib.negateX";
    public static final String REFLECT_Y_FACTOR_NAME = "reflectionCalib.negateY";
    
    public static CalibrationProperties obtainCalibrationProperties(Properties specificProps, Properties defaultProps){
        
        CalibrationProperties defaultP = new CalibrationProperties(defaultProps);
        CalibrationProperties specificP = new CalibrationProperties(specificProps);
        
        Quaternion outputRotCalib = (Quaternion)chooseSpecificOrDefault(
                specificP.rotationCalib,defaultP.rotationCalib);
        Float outputRealToProbe = (Float)chooseSpecificOrDefault(
                specificP.probeToRealFactor,defaultP.probeToRealFactor);
        Float outputVirtualToProbe = (Float)chooseSpecificOrDefault(
                specificP.realToVirtualFactor,defaultP.realToVirtualFactor);
        
        Boolean reflectX = (Boolean)chooseSpecificOrDefault(specificP.reflectX,defaultP.reflectX);
        Boolean reflectY = (Boolean)chooseSpecificOrDefault(specificP.reflectY,defaultP.reflectY);
        
        return new CalibrationProperties(outputRotCalib,outputRealToProbe,outputVirtualToProbe,reflectX,reflectY);
    }
    
    private CalibrationProperties(Quaternion rotationCalib, Float realToProbeFactor, Float virtualToRealFactor, Boolean reflectX, Boolean reflectY){
        this.probeToRealFactor = realToProbeFactor;
        this.rotationCalib = rotationCalib;
        this.realToVirtualFactor = virtualToRealFactor;
        this.reflectX = reflectX;
        this.reflectY = reflectY;
        
        scaleFactor = realToProbeFactor*virtualToRealFactor;
        scaleFactorX = DisplacementHelper.negativeIfTrue(scaleFactor, reflectX);
        scaleFactorY = DisplacementHelper.negativeIfTrue(scaleFactor, reflectY);
    }
    
    private CalibrationProperties(Properties props){
        
        if(props == null) return;
        
        if(props.containsKey(PROBE_TO_REAL_FACTOR_NAME)){
            probeToRealFactor = PropertiesHelper.getFloatValueProperty(props, PROBE_TO_REAL_FACTOR_NAME);
        }
        if(props.containsKey(REAL_TO_VIRTUAL_FACTOR_NAME)){
            realToVirtualFactor = PropertiesHelper.getFloatValueProperty(props, REAL_TO_VIRTUAL_FACTOR_NAME);
        }
        
        if(props.containsKey(REFLECT_X_FACTOR_NAME)){
            reflectX = PropertiesHelper.getBooleanValueProperty(props, REFLECT_X_FACTOR_NAME);
        }
        if(props.containsKey(REFLECT_Y_FACTOR_NAME)){
            reflectY = PropertiesHelper.getBooleanValueProperty(props, REFLECT_Y_FACTOR_NAME);
        }
        
        if(props.containsKey(QUATERNION_W_FACTOR_NAME) && 
                props.containsKey(QUATERNION_X_FACTOR_NAME) && 
                props.containsKey(QUATERNION_Y_FACTOR_NAME) && 
                props.containsKey(QUATERNION_Z_FACTOR_NAME)){

            rotationCalib = new Quaternion(
                    PropertiesHelper.getFloatValueProperty(props, QUATERNION_X_FACTOR_NAME),
                    PropertiesHelper.getFloatValueProperty(props, QUATERNION_Y_FACTOR_NAME),
                    PropertiesHelper.getFloatValueProperty(props, QUATERNION_Z_FACTOR_NAME),
                    PropertiesHelper.getFloatValueProperty(props, QUATERNION_W_FACTOR_NAME));
        }
        
        
        
    }
    
    public static Object chooseSpecificOrDefault(Object specificObj, Object defaultObj){
        if(specificObj != null){
            return specificObj;
        }else{
            return defaultObj;
        }
    }
    
    public static ArrayList<String> getCalibrationPropertiesStrings(Quaternion calib){
        ArrayList<String> returnString = new ArrayList<String>(4);
        returnString.add(getPropertiesString(QUATERNION_W_FACTOR_NAME,calib.getW()));
        returnString.add(getPropertiesString(QUATERNION_X_FACTOR_NAME,calib.getX()));
        returnString.add(getPropertiesString(QUATERNION_Y_FACTOR_NAME,calib.getY()));
        returnString.add(getPropertiesString(QUATERNION_Z_FACTOR_NAME,calib.getZ()));
        return returnString;
    }
    private static String getPropertiesString(String name, float value){
        return name + "=" + String.valueOf(value);
    }

    public Quaternion getRotationCalib() {
        return rotationCalib;
    }
    
    public Float getScaleFactor(){
        return scaleFactor;
    }
    
    public Float getScaleFactorX(){
        return scaleFactorX;
    }
    
    public Float getScaleFactorY(){
        return scaleFactorY;
    }
}
