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
    
    public CalibrationFileResults(Properties props){
        
        //gets the scale factors
        float realToProbeFactor = Float.parseFloat(
                props.getProperty("scaleFactor.realToProbe"));
        float virtualToRealFactor = Float.parseFloat(
                props.getProperty("scaleFactor.virtualToReal"));
        scaleCalib = realToProbeFactor*virtualToRealFactor;
        
        //gets the starting quaternion
        float quatW = Float.parseFloat(props.getProperty("initQuat.w"));
        float quatX = Float.parseFloat(props.getProperty("initQuat.x"));
        float quatY = Float.parseFloat(props.getProperty("initQuat.y"));
        float quatZ = Float.parseFloat(props.getProperty("initQuat.z"));
        rotationCalib = new Quaternion(quatX,quatY,quatZ,quatW);
        
        
    }

    public Quaternion getRotationCalib() {
        return rotationCalib;
    }

    public float getScaleCalib() {
        return scaleCalib;
    }
    
}
