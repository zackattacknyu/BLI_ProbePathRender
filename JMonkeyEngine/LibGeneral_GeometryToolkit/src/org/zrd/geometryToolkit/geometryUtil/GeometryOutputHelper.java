/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.geometryUtil;

import com.jme3.math.Vector3f;
import org.zrd.util.dataHelp.BasicAngleHelper;

/**
 *
 * @author Zach
 */
public class GeometryOutputHelper {

    public static String getYawPitchRollDisplayString(float yawInRadians, float pitchInRadians, float rollInRadians) {
        
        float yawInDegs = BasicAngleHelper.convertRadiansToDegrees(yawInRadians);
        float pitchInDegs = BasicAngleHelper.convertRadiansToDegrees(pitchInRadians);
        float rollInDegs = BasicAngleHelper.convertRadiansToDegrees(rollInRadians);
        
        int displayYaw = roundToNearestEven(yawInDegs);
        int displayPitch = roundToNearestEven(pitchInDegs);
        int displayRoll = roundToNearestEven(rollInDegs);
        
        return String.format("(Yaw,Pitch,Roll) = (%1$d,%2$d,%3$d)", displayYaw,displayPitch,displayRoll);
    }
    
    public static int roundToNearestEven(float num){
        return 2*((int)Math.floor(num/2.0));
    }

    public static String getXYZDisplayString(Vector3f position) {
        return String.format("(X,Y,Z) = (%1$.2f,%2$.2f,%3$.2f)", 
                position.getX(),position.getY(),position.getZ());
    }
    
}
