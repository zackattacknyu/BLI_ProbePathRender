/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.cmdapplication_testing;

import com.jme3.math.Quaternion;

/**
 *
 * @author BLI
 */
public class CMDApplication_Testing {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Quaternion q1 = new Quaternion(-0.30451342f, 0.32911694f, 0.89337474f, 0.028897978f);
        Quaternion q2 = new Quaternion(0.35617307f, 0.41199765f, 0.83862805f, 0.0101691345f);
        Quaternion q3 = new Quaternion(-0.34368083f, 0.51553464f, 0.78439593f, 0.028822815f);
        
        System.out.println("Q1 to Q2 slerp: " + q1.slerp(q1, q2, 0.5f));
        System.out.println("Q3: " + q3);
    }
}
