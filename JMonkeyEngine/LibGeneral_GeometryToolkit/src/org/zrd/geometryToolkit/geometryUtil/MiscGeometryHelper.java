/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.geometryUtil;

import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import org.zrd.util.dataHelp.BasicAngleHelper;

/**
 *
 * @author BLI
 */
public class MiscGeometryHelper {

    public static Matrix3f getRotationMatrix(float angle, Vector3f axis) {
        Quaternion rotation = new Quaternion();
        rotation.fromAngleAxis(angle, axis);
        return rotation.toRotationMatrix();
    }
    
    public static ArrayList<Vector3f> getCopyOfPath(ArrayList<Vector3f> path){
        ArrayList<Vector3f> returnPath = new ArrayList<Vector3f>(path.size());
        for(Vector3f vertex : path){
            returnPath.add(vertex.clone());
        }
        return returnPath;
    }
    
    public static String getXYZDisplayString(Vector3f position){
        return "(X,Y,Z) = " + position;
    }
    
    public static String getYawPitchRollDisplayString(float yawInRadians, float pitchInRadians, float rollInRadians){
        
        //makes a vector out of the orientation point
        //      to take advantage of its toString method
        Vector3f orientationPoint = new Vector3f(
                BasicAngleHelper.convertRadiansToDegrees(yawInRadians),
                BasicAngleHelper.convertRadiansToDegrees(pitchInRadians),
                BasicAngleHelper.convertRadiansToDegrees(rollInRadians));
        
        return "(Yaw,Pitch,Roll) = " + orientationPoint;
    }
    
}
