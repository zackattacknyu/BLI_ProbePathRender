/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.bliProbePath;

import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author BLI
 */
public class RotationTesting {
    public static void doRotationTesting(){
        float yaw = (float)(Math.random()*(Math.PI/2.0));
        float pitch = (float)(Math.random()*(Math.PI/2.0));
        float roll = (float)(Math.random()*(Math.PI/2.0));
        
        Quaternion yawQ = new Quaternion();
        yawQ.fromAngleAxis(yaw, Vector3f.UNIT_Z);
        Matrix3f yawQMat = yawQ.toRotationMatrix();
        
        Quaternion pitchQ = new Quaternion();
        pitchQ.fromAngleAxis(pitch, Vector3f.UNIT_X);
        Matrix3f pitchQMat = pitchQ.toRotationMatrix();
        
        Quaternion rollQ = new Quaternion();
        rollQ.fromAngleAxis(roll, Vector3f.UNIT_Y);
        Matrix3f rollQMat = rollQ.toRotationMatrix();
        
        Quaternion quat = (yawQ.mult(pitchQ)).mult(rollQ);
        Matrix3f quatMat = quat.toRotationMatrix();
        Matrix3f rotMat = (yawQMat.mult(pitchQMat)).mult(rollQMat);
        
        System.out.println("(Yaw,Pitch,Roll)=(" + yaw + "," + pitch + "," + roll + ")");
        System.out.println("Rotation Matrix with quat " + quatMat);
        System.out.println("Rotation Matrix without quat: " + rotMat);
        
        float x,y,z,xAng,yAng,zAng;
        Vector3f vec;
        for(int tryNum = 1; tryNum <= 10; tryNum++){
            x = (float) (Math.random()*5);
            y = (float) (Math.random()*5);
            z = (float) (Math.random()*5);
            xAng = (float) (Math.random()*Math.PI);
            yAng = (float) (Math.random()*Math.PI);
            zAng = (float) (Math.random()*Math.PI);
            vec = new Vector3f(x,y,z);
            quat = new Quaternion();
            quat.fromAngles(xAng, yAng, zAng);
            System.out.println("Original Vector: " + vec);
            System.out.println("New Vector from Quat Mult: " + quat.mult(vec));
            System.out.println("New Vector using Rotation Matrix: " + quat.toRotationMatrix().mult(vec));
        }
        
        
    }
}
