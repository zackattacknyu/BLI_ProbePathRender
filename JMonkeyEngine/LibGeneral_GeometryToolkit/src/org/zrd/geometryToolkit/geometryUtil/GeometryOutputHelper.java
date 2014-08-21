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
        
        int yawInDegs = BasicAngleHelper.convertRadiansToIntDegrees(yawInRadians);
        int pitchInDegs = BasicAngleHelper.convertRadiansToIntDegrees(pitchInRadians);
        int rollInDegs = BasicAngleHelper.convertRadiansToIntDegrees(rollInRadians);
        
        return String.format("(Yaw,Pitch,Roll) = (%s,%s,%s)", yawInDegs,pitchInDegs,rollInDegs);
    }

    public static String getXYZDisplayString(Vector3f position) {
        return "(X,Y,Z) = " + position;
    }
    
}
