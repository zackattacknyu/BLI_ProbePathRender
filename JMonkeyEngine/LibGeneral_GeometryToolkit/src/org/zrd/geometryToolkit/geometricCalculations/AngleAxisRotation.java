/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.geometricCalculations;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**This represents a rotation by its axis and angle
 * 
 * * Derived from this wikipedia page:
     * http://en.wikipedia.org/wiki/Rotation_formalisms_in_three_dimensions
     * 
     * It is also described here well:
     * http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToAngle/
 *
 * @author Zach
 */
public class AngleAxisRotation {
    private Vector3f axis;
    private float angle;
    private Quaternion quatRep;
    
    public AngleAxisRotation(Quaternion quat){
        this.quatRep = quat.clone();
        angle = getAngle(quatRep);
        axis = getAxis(quatRep);
    }
    
    public AngleAxisRotation(Vector3f axis, float angle){
        this.axis = axis;
        this.angle = angle;
        quatRep = new Quaternion();
        quatRep.fromAngleAxis(angle, axis);
    }
    
    public Quaternion getQuat(){
        return quatRep;
    }
    
    public static void normalizeQuatIfNeeded(Quaternion quat){
        if(quat.getW() > 1) quat.normalizeLocal();
    }
    
    public static Vector3f getAxis(Quaternion quat){
        normalizeQuatIfNeeded(quat);
        float s = (float)Math.sqrt(1-quat.getW()*quat.getW());
        float x,y,z;
        if (s < MathConstants.EPSILON) { 
            x = quat.getX(); 
            y = quat.getY();
            z = quat.getZ();
        } else {
            x = quat.getX() / s; // normalise axis
            y = quat.getY() / s;
            z = quat.getZ() / s;
        }
        return new Vector3f(x,y,z);
    }
    
    public static float getAngle(Quaternion quat){
        normalizeQuatIfNeeded(quat);
        return (float)(2 * Math.acos(quat.getW()));
    }

    public Vector3f getAxis() {
        return axis;
    }

    public float getAngle() {
        return angle;
    }

    
}
