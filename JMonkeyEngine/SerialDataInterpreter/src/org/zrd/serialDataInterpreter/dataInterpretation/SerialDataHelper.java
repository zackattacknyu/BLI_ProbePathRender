/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataInterpretation;

/**
 *
 * @author BLI
 */
public class SerialDataHelper {

    public static final float UNIFORM_SCALE_FACTOR = 1000.0f;
    public static final float DEG_TO_RAD_FACTOR = (float) (Math.PI/180.0);
    
    public static float getReturnDisp(float disp){
        return disp/UNIFORM_SCALE_FACTOR;
    }
    
    
    public static float getReturnAngle(float initAngle){
        return initAngle*DEG_TO_RAD_FACTOR;
    }
    
    
}
