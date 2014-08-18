/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.geometryUtil;

import com.jme3.math.Quaternion;
import java.util.ArrayList;
import java.util.Properties;
import org.zrd.util.properties.PropertiesHelper;

/**
 *
 * @author BLI
 */
public class CalibrationProperties {
    
    private Quaternion rotationCalib;
    private Float realToProbeFactor;
    private Float virtualToRealFactor;
    
    public static final String REAL_TO_PROBE_FACTOR_NAME = "scaleFactor.realToProbe";
    public static final String VIRTUAL_TO_REAL_FACTOR_NAME = "scaleFactor.virtualToReal";
    
    public static final String QUATERNION_W_FACTOR_NAME = "initQuat.w";
    public static final String QUATERNION_X_FACTOR_NAME = "initQuat.x";
    public static final String QUATERNION_Y_FACTOR_NAME = "initQuat.y";
    public static final String QUATERNION_Z_FACTOR_NAME = "initQuat.z";
    
    public static CalibrationProperties obtainCalibrationProperties(Properties specificProps, Properties defaultProps){
        
        CalibrationProperties defaultP = new CalibrationProperties(defaultProps);
        CalibrationProperties specificP = new CalibrationProperties(specificProps);
        
        Quaternion outputRotCalib = (Quaternion)chooseSpecificOrDefault(
                specificP.rotationCalib,defaultP.rotationCalib);
        Float outputRealToProbe = (Float)chooseSpecificOrDefault(
                specificP.realToProbeFactor,defaultP.realToProbeFactor);
        Float outputVirtualToProbe = (Float)chooseSpecificOrDefault(
                specificP.virtualToRealFactor,defaultP.virtualToRealFactor);
        
        return new CalibrationProperties(outputRotCalib,outputRealToProbe,outputVirtualToProbe);
    }
    
    private CalibrationProperties(Quaternion rotationCalib, Float realToProbeFactor, Float virtualToRealFactor){
        this.realToProbeFactor = realToProbeFactor;
        this.rotationCalib = rotationCalib;
        this.virtualToRealFactor = virtualToRealFactor;
    }
    
    private CalibrationProperties(Properties props){
        
        if(props == null) return;
        
        if(props.containsKey(REAL_TO_PROBE_FACTOR_NAME)){
            realToProbeFactor = PropertiesHelper.getFloatValueProperty(props, REAL_TO_PROBE_FACTOR_NAME);
        }
        if(props.containsKey(VIRTUAL_TO_REAL_FACTOR_NAME)){
            virtualToRealFactor = PropertiesHelper.getFloatValueProperty(props, VIRTUAL_TO_REAL_FACTOR_NAME);
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

    public Float getRealToProbeFactor() {
        return realToProbeFactor;
    }

    public Float getVirtualToRealFactor() {
        return virtualToRealFactor;
    }
    
    public Float getScaleCalibration(){
        return realToProbeFactor*virtualToRealFactor;
    }
    
}
