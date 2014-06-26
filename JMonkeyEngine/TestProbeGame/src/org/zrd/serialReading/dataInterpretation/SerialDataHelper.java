/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialReading.dataInterpretation;

import com.jme3.math.FastMath;

/**
 *
 * @author BLI
 */
public class SerialDataHelper {

    public static final float uniformScaleFactor = 1000.0f;
    
    public static float getReturnDisp(float disp){
        return disp/1000.0f;
    }
    
    
    public static float getReturnAngle(float initAngle){
        return initAngle*FastMath.DEG_TO_RAD;
    }
    
    
}
