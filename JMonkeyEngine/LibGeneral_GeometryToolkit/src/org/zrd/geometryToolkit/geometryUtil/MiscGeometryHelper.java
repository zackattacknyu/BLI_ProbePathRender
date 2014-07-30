/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.geometryUtil;

import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
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

    /**
     * This solves the following matrix equation:
     *      [ a b ][s]  = [e]
     *      [ c d ][t]    [f]
     *
     * It will return a vector in the form (s,t)
     *  and it takes in a,b,c,d,e,f
     * @param a
     * @param b
     * @param c
     * @param d
     * @param e
     * @param f
     * @return
     */
    public static Vector2f solveMatrixEqu(float a, float b, float c, float d, float e, float f) {
        float det = a * d - b * c;
        float absDet = (float) Math.abs(det);
        if (absDet < GeometryToolkitConstants.EPSILON) {
            return null;
        }
        float s = (d * e - b * f) / det;
        float t = (a * f - e * c) / det;
        return new Vector2f(s, t);
    }
    
}
