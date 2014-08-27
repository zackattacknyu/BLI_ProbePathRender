/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.cmdapplication_testing;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import org.zrd.geometryToolkit.geometricCalculations.AngleAxisRotation;
import org.zrd.geometryToolkit.geometryUtil.CalibrationProperties;
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
        
        Quaternion q1 = new Quaternion(0.9011022f,-0.07613133f,-0.009502405f,0.42676535f);
        
        //rotate 90 degrees by normal as axis
        AngleAxisRotation rot90normal = new AngleAxisRotation(
                new Vector3f(-0.0745382f, -0.80956864f, -0.5822738f), 
                (float) (Math.PI/2));
        Quaternion q2 = rot90normal.getQuat();

        //rotate 180 degress by x-axis
        /*AngleAxisRotation rot180x = new AngleAxisRotation(
                new Vector3f(-0.7600107f, 0.4200855f, -0.4958947f), 
                (float) (Math.PI));*/
        AngleAxisRotation rot180x = new AngleAxisRotation(
                new Vector3f(0.7600107f, -0.4200855f, 0.4958947f), 
                (float) (-Math.PI));
        Quaternion q3 = rot180x.getQuat();
        
        AngleAxisRotation rot90y = new AngleAxisRotation(new Vector3f(-0.58160686f, -0.43028995f, 0.69035107f),(float)(Math.PI/2));
        Quaternion q4 = rot90y.getQuat();
        
        Quaternion rotCalib = q4.mult(q3.mult(q2.mult(q1)));
        
        OutputHelper.printStringCollection(CalibrationProperties.getCalibrationPropertiesStrings(rotCalib));
    }

}
