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
        
        return String.format("(Yaw,Pitch,Roll) = (%1$.0f,%2$.0f,%3$.0f)", yawInDegs,pitchInDegs,rollInDegs);
    }

    public static String getXYZDisplayString(Vector3f position) {
        return String.format("(X,Y,Z) = (%1$.2f,%2$.2f,%3$.2f)", 
                position.getX(),position.getY(),position.getZ());
    }
    
}
