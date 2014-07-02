/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking.deviceToWorldConversion;

import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import org.zrd.graphicsTools.geometry.meshTraversal.MeshHelper;

/**
 *
 * @author BLI
 */
public class TrackingHelper {
    
    public static Vector3f getXYDisplacement(float deltaX, float deltaY, float yawInRadians){
        
        Vector3f mouseDisp = new Vector3f(deltaX,deltaY,0);
        Matrix3f rotMatrix = getQuaternion(yawInRadians).toRotationMatrix();
        
        return rotMatrix.mult(mouseDisp);
        
    }
    
    public static Vector3f getXYZDisplacement(float deltaX, float deltaY, Vector3f currentNormal, Quaternion localRotation){
        
        Vector3f xVector = new Vector3f(1,0,0);
        
        Vector3f xDispProj = MeshHelper.getVectorProjOnPlane(currentNormal, xVector);
        xDispProj.normalizeLocal();
        
        Vector3f yDispProj = currentNormal.cross(xDispProj);
        yDispProj.normalizeLocal();
        
        Vector3f totalXDisp = xDispProj.mult(deltaX);
        Vector3f totalYDisp = yDispProj.mult(deltaY);
        
        if(Math.abs(deltaX) > 0 || Math.abs(deltaY) > 0){
            System.out.println("change in (x,y) = (" + deltaX + "," + deltaY + ")");
            System.out.println("Normal is " + currentNormal);
            System.out.println("X Disp Proj: " + xDispProj);
            System.out.println("Y Disp Proj: " + yDispProj);
        }
        
        return totalXDisp.add(totalYDisp);
        
    }
    
    public static Vector3f getXYZDisplacement(float deltaX, float deltaY, Quaternion localRotation){
        
        
        
        Vector3f mouseDisp = new Vector3f(0,deltaX,deltaY);
        Matrix3f rotMatrix = localRotation.toRotationMatrix();
        Vector3f newDisp = rotMatrix.mult(mouseDisp);
        
        
        return newDisp;
        
    }
    
    public static Vector3f getDisplacement(float deltaX, float deltaY, Vector3f xAxis, Vector3f yAxis){
        Vector3f xDisp = xAxis.mult(deltaX);
        Vector3f yDisp = yAxis.mult(deltaY);
        return xDisp.add(yDisp);
    }
    
    
    
    
    public static Vector2f scaleXYDisplacement(Vector2f displacement, float xScale, float yScale){
        float xVal = displacement.getX();
        float yVal = displacement.getY();
        return new Vector2f(xVal*xScale,yVal*yScale);
    }
    
    public static Matrix3f getRotationMatrix(float yawInRadians){
        
        return getQuaternion(yawInRadians).toRotationMatrix();
        
        
    }
    
    public static Matrix3f getRotationmatrix(float yawInRadians){
        return getQuaternion(yawInRadians).toRotationMatrix();
    }
    
    public static Matrix3f getRotationMatrix(float yawInRadians,float pitchInRadians, float rollInRadians){
        return getQuaternion(yawInRadians,pitchInRadians,rollInRadians).toRotationMatrix();
    }
    
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
    
    public static float getYaw(Quaternion rotation){
        return rotation.toAngleAxis(Vector3f.UNIT_Z);
    }
    public static float getPitch(Quaternion rotation){
        return rotation.toAngleAxis(Vector3f.UNIT_X);
    }
    public static float getRoll(Quaternion rotation){
        return rotation.toAngleAxis(Vector3f.UNIT_Y);
    }
    
}
