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
        
        /*Quaternion rotation = new Quaternion();
        rotation.fromAngles(pitchInRadians, rollInRadians, yawInRadians);
        return rotation;*/
        
        
        Quaternion yaw = new Quaternion();
        yaw.fromAngleAxis(yawInRadians, Vector3f.UNIT_Z);
        
        Quaternion pitch = new Quaternion();
        pitch.fromAngleAxis(pitchInRadians, Vector3f.UNIT_X);
        
        Quaternion roll = new Quaternion();
        roll.fromAngleAxis(rollInRadians, Vector3f.UNIT_Y);
        
        return (yaw.mult(pitch)).mult(roll);
        
        //return (roll.mult(pitch)).mult(yaw);
        
        
    }
}
