/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataInterpretation;

import com.jme3.math.FastMath;

/**
 *
 * @author BLI
 */
public class SerialDataHelper {

    public static final float initXscaleFactor = 1000.0f;
    public static final float initYscaleFactor = 1000.0f;
    
    public static float getReturnX(float originalX){
        return originalX/initXscaleFactor;
    }    
    
    public static float getReturnY(float originalY){
        return originalY/initYscaleFactor;
    }
    
    public static float getReturnAngle(float initAngle){
        return initAngle*FastMath.DEG_TO_RAD;
    }
    
    
}
