/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.cmdapplication_testing;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author BLI
 */
public class CMDApplication_Testing {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*Quaternion q1 = new Quaternion(-0.30451342f, 0.32911694f, 0.89337474f, 0.028897978f);
        Quaternion q2 = new Quaternion(0.35617307f, 0.41199765f, 0.83862805f, 0.0101691345f);
        Quaternion q3 = new Quaternion(-0.34368083f, 0.51553464f, 0.78439593f, 0.028822815f);
        
        System.out.println("Q1 to Q2 slerp: " + q1.slerp(q1, q2, 0.5f));
        System.out.println("Q3: " + q3);*/
        
        /*float yawInDegs = 27.568956f;
        float pitchInDegs = 85.6923f;
        float rollInDegs = 120.569874f;
        
        System.out.println(String.format("(Yaw,Pitch,Roll) = (%1$.1f,%2$.1f,%3$.1f)", yawInDegs,pitchInDegs,rollInDegs));
        
        System.out.println((400)%360);
        Quaternion q1 = new Quaternion(-0.273331f, -0.28909302f, -0.5671392f, 0.7211577f);
        Quaternion q2 = new Quaternion(-0.24983868f, 0.010499869f, 0.646246f,0.7209971f);
        System.out.println(q1.mult(q2));*/
        
        Vector3f desiredNormal = new Vector3f(-0.0745382f, -0.80956864f, -0.5822738f);
        Vector3f actualNormal = new Vector3f(0.016116796f, 0.066235244f, 0.997674f);
        
        Quaternion quat = getRotationFromVectors(actualNormal,desiredNormal);
        System.out.println("Quaternion: " + quat);
        
        
    }
    
    /**
     * Given an actual direction and desired direction, this method
     *      computes the rotation matrix that will rotate the actual
     *      vector to the desired vector.
     *
     * @param actualDir
     * @param desiredDir
     * @return
     */
    public static Quaternion getRotationFromVectors(Vector3f actualDir, Vector3f desiredDir) {
        float cosTheta = desiredDir.dot(actualDir);
        float rotAngle = (float) Math.acos(cosTheta);
        Vector3f rotAxis = desiredDir.cross(actualDir);
        rotAxis = rotAxis.normalize();
        Quaternion rotQuaternion = new Quaternion();
        rotQuaternion.fromAngleAxis(-1 * rotAngle, rotAxis);
        return rotQuaternion;
    }
}
