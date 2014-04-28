/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author BLI
 */
public class TrackingHelper {
    
    public static Vector3f getXYDisplacement(float deltaX, float deltaY, float yawInRadians){
        
        Vector3f mouseDisp = new Vector3f(deltaX,deltaY,0);
        Matrix3f rotMatrix = getQuarternion(yawInRadians).toRotationMatrix();
        
        return rotMatrix.mult(mouseDisp);
        
    }
    
    public static Vector3f getXYZDisplacement(float deltaX, float deltaY, Quaternion localRotation){
        
        Vector3f mouseDisp = new Vector3f(deltaX,deltaY,0);
        Matrix3f rotMatrix = localRotation.toRotationMatrix();
        
        return rotMatrix.mult(mouseDisp);
        
    }
    
    public static Vector3f scaleDisplacement(Vector3f displacement, float xScale, float yScale){
        float xVal = displacement.getX();
        float yVal = displacement.getY();
        float zVal = displacement.getZ();
        
        float zScale = 0.02f;
        
        return new Vector3f(xVal*xScale,yVal*yScale,zVal*zScale);
    }
    
    public static Vector3f scaleDisplacement(Vector3f displacement, float xScale, float yScale, float zScale){
        float xVal = displacement.getX();
        float yVal = displacement.getY();
        float zVal = displacement.getZ();
        
        return new Vector3f(xVal*xScale,yVal*yScale,zVal*zScale);
    }
    
    public static Matrix3f getRotationMatrix(float yawInRadians){
        
        return getQuarternion(yawInRadians).toRotationMatrix();
        
        
    }
    
    public static Quaternion getQuarternion(float yawInRadians){
        
        Quaternion output = new Quaternion();
        output.fromAngleAxis(yawInRadians, Vector3f.UNIT_Z);
        return output;
    }
    
    public static Quaternion getQuarternion(float yawInRadians, 
            float pitchInRadians, float rollInRadians){
        
        Quaternion yaw = new Quaternion();
        yaw.fromAngleAxis(yawInRadians, Vector3f.UNIT_Z);
        
        Quaternion pitch = new Quaternion();
        pitch.fromAngleAxis(pitchInRadians, Vector3f.UNIT_X);
        
        Quaternion roll = new Quaternion();
        roll.fromAngleAxis(rollInRadians, Vector3f.UNIT_Y);
        
        Quaternion yawPitch = yaw.mult(pitch);
        
        return yawPitch.mult(roll);
    }
    
}
