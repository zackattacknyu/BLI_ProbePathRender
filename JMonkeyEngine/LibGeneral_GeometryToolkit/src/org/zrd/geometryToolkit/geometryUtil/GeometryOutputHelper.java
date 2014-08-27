/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.geometryUtil;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import org.zrd.util.dataHelp.BasicAngleHelper;

/**
 *
 * @author Zach
 */
public class GeometryOutputHelper {

    public static String getYawPitchRollDisplayString(float yawInRadians, float pitchInRadians, float rollInRadians) {
        
        int yawInDegs = BasicAngleHelper.convertRadiansToIntDegrees(yawInRadians);
        int pitchInDegs = BasicAngleHelper.convertRadiansToIntDegrees(pitchInRadians);
        int rollInDegs = BasicAngleHelper.convertRadiansToIntDegrees(rollInRadians);
        
        return String.format("(Yaw,Pitch,Roll) = (%s,%s,%s)", yawInDegs,pitchInDegs,rollInDegs);
    }

    public static String getXYZDisplayString(Vector3f position) {
        return String.format("(X,Y,Z) = (%1$.2f,%2$.2f,%3$.2f)", 
                position.getX(),position.getY(),position.getZ());
    }
    
    public static ArrayList<String> getMatrixDisplayStrings(Matrix3f matrix){
        ArrayList<String> outputStrings = new ArrayList<String>(3);
        outputStrings.add(String.valueOf(matrix.getRow(0)));
        outputStrings.add(String.valueOf(matrix.getRow(1)));
        outputStrings.add(String.valueOf(matrix.getRow(2)));
        return outputStrings;
    }
    
}
