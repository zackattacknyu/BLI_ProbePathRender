/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.probeCalibration;

import com.jme3.math.Matrix4f;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometricCalculations.ScaleTransformHelper;
import org.zrd.geometryToolkit.geometricCalculations.TransformHelper;
import org.zrd.geometryToolkit.pathTools.PathTransformHelper;

/**
 *
 * @author BLI
 */
public class ScaleCalibration {
    
    private float scaleFactorX;
    private float scaleFactorY;
    private float uniformScaleFactor;

    private ArrayList<Vector3f> scaledPath;
    
    public ScaleCalibration(ArrayList<Vector3f> inputPath, Vector3f newEndpoint){
        Vector3f startPoint = inputPath.get(0);
        Vector3f oldEndpoint = inputPath.get(inputPath.size() - 1);
        float currentLength = oldEndpoint.distance(startPoint);
        float desiredLength = newEndpoint.distance(startPoint);
        uniformScaleFactor = desiredLength / currentLength;
        displayScaleFactor(uniformScaleFactor);
        Matrix4f transform = ScaleTransformHelper.getScaleAroundPoint(startPoint, uniformScaleFactor);
        scaledPath = PathTransformHelper.getTransformedVertices(inputPath, transform);
    }
    
    public static ArrayList<Vector3f> scalePathForNewEndpoint(ArrayList<Vector3f> inputPath, Vector3f newEndpoint) {
        ScaleCalibration calib = new ScaleCalibration(inputPath,newEndpoint);
        return calib.getScaledPath();
    }
    
    public float getUniformScaleFactor(){
        return uniformScaleFactor;
    }

    public ArrayList<Vector3f> getScaledPath() {
        return scaledPath;
    }

    public static void displayScaleFactor(float scaleFactor) {
        System.out.println("Scale Factor was: " + scaleFactor);
    }
    
    
    
}
