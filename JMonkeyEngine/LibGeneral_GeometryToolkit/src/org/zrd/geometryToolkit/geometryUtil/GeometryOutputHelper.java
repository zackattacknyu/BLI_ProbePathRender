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
        Vector3f orientationPoint = new Vector3f(BasicAngleHelper.convertRadiansToDegrees(yawInRadians), BasicAngleHelper.convertRadiansToDegrees(pitchInRadians), BasicAngleHelper.convertRadiansToDegrees(rollInRadians));
        return "(Yaw,Pitch,Roll) = " + orientationPoint;
    }

    public static String getXYZDisplayString(Vector3f position) {
        return "(X,Y,Z) = " + position;
    }
    
}
