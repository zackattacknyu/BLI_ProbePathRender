/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.geometricCalculations;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
//import org.zrd.graphicsTools.geometry.meshTraversal.MeshHelper;

/**
 *
 * @author BLI
 */
public class RotationHelper {
    
    public static Quaternion getQuaternion(float yawInRadians){
        
        Quaternion output = new Quaternion();
        output.fromAngleAxis(yawInRadians, Vector3f.UNIT_Z);
        return output;
    }
    
    public static Quaternion getQuaternion(float yawInRadians, 
            float pitchInRadians, float rollInRadians){

        Quaternion rot = new Quaternion();
        return rot.fromAngles(pitchInRadians, rollInRadians, yawInRadians);
        
        
    }
}
