/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.geometryUtil;

import com.jme3.math.Quaternion;
import java.util.Properties;

/**
 *
 * @author BLI
 */
public class CalibrationFileResults {
    
    private Quaternion rotationCalib;
    private float scaleCalib;
    
    private String REAL_TO_PROBE_FACTOR_NAME = "scaleFactor.realToProbe";
    private String VIRTUAL_TO_REAL_FACTOR_NAME = "scaleFactor.virtualToReal";
    
    private String QUATERNION_W_FACTOR_NAME = "initQuat.w";
    private String QUATERNION_X_FACTOR_NAME = "initQuat.x";
    private String QUATERNION_Y_FACTOR_NAME = "initQuat.y";
    private String QUATERNION_Z_FACTOR_NAME = "initQuat.z";
    
    public CalibrationFileResults(Properties props){
        
        //gets the scale factors
        float realToProbeFactor = 1;
        float virtualToRealFactor = 1;
        
        if(props.containsKey(REAL_TO_PROBE_FACTOR_NAME)){
            realToProbeFactor = Float.parseFloat(
                props.getProperty(REAL_TO_PROBE_FACTOR_NAME));
        }
        if(props.containsKey(VIRTUAL_TO_REAL_FACTOR_NAME)){
            virtualToRealFactor = Float.parseFloat(
                props.getProperty(VIRTUAL_TO_REAL_FACTOR_NAME));
        }
        scaleCalib = realToProbeFactor*virtualToRealFactor;
        
        //gets the starting quaternion
        float quatW = 1;
        float quatX = 0;
        float quatY = 0;
        float quatZ = 0;
        
        if(props.containsKey(QUATERNION_W_FACTOR_NAME) && 
                props.containsKey(QUATERNION_X_FACTOR_NAME) && 
                props.containsKey(QUATERNION_Y_FACTOR_NAME) && 
                props.containsKey(QUATERNION_Z_FACTOR_NAME)){
            
            quatW = Float.parseFloat(props.getProperty(QUATERNION_W_FACTOR_NAME));
            quatX = Float.parseFloat(props.getProperty(QUATERNION_X_FACTOR_NAME));
            quatY = Float.parseFloat(props.getProperty(QUATERNION_Y_FACTOR_NAME));
            quatZ = Float.parseFloat(props.getProperty(QUATERNION_Z_FACTOR_NAME));
            rotationCalib = new Quaternion(quatX,quatY,quatZ,quatW);
        }
        
        
        
    }

    public Quaternion getRotationCalib() {
        return rotationCalib;
    }

    public float getScaleCalib() {
        return scaleCalib;
    }
    
}
