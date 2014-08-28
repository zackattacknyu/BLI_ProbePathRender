/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.cmdapplication_testing;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import org.zrd.geometryToolkit.geometricCalculations.AngleAxisRotation;
import org.zrd.geometryToolkit.geometricCalculations.RotationTransformHelper;
import org.zrd.geometryToolkit.geometryUtil.CalibrationProperties;
import org.zrd.util.dataHelp.BasicAngleHelper;
import org.zrd.util.dataHelp.OutputHelper;

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
        
        System.out.println(String.format("(Yaw,Pitch,Roll) = (%1$.1f,%2$.1f,%3$.1f)", yawInDegs,pitchInDegs,rollInDegs));*/
               
        //rotate the normal of the probe to match a triangle normal
        Vector3f triangleNorm = new Vector3f(-0.0745382f, -0.80956864f, -0.5822738f);
        Vector3f probeNormal = new Vector3f(-0.06540184f, -0.06040227f, 0.99602926f);
        Quaternion q1 = RotationTransformHelper.getRotationFromVectors(probeNormal, triangleNorm);
        
        //rotate the yaw angle to match the correct one
        AngleAxisRotation rotNormal = new AngleAxisRotation(triangleNorm, 
                BasicAngleHelper.convertDegreesToRadians(106));
        Quaternion q2 = rotNormal.getQuat();
        
        //rotation calibration
        Quaternion rotCalib = q2.mult(q1);
        
        OutputHelper.printStringCollection(CalibrationProperties.getCalibrationPropertiesStrings(rotCalib));
    }

}
