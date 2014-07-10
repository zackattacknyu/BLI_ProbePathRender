/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.graphicsTools.geometry.path;

import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import org.zrd.graphicsTools.geometry.meshTraversal.MeshHelper;
import org.zrd.graphicsTools.geometry.util.AngleAxisRotation;
import org.zrd.utilImpl.general.ProgramConstants;

/**
 *
 * @author Zach
 */
public class PathTransformHelper {
    
    public static ArrayList<Vector3f> transformPathEndpoint(ArrayList<Vector3f> inputPath, Vector3f newEndpoint){
        Matrix4f wholeTransformation = getTransformOfEndpoint(inputPath,newEndpoint);
        displayAngleOfRotation(wholeTransformation);
        return MeshHelper.getTransformedVertices(inputPath, wholeTransformation);
    }
    
    /**
     * This takes in a path and returns the matrix transform of the points
     *      that will make it so that the endpoint of the path matches
     *      the input endpoint while the start point is the same.
     * @param inputPath         original path
     * @param newEndpoint       desired endpoint of the path
     * @return          the transformation that will keep the start points but change the endpoint to the desired one
     */
    public static Matrix4f getTransformOfEndpoint(ArrayList<Vector3f> inputPath, Vector3f newEndpoint){
        Vector3f startPoint = inputPath.get(0);
        Vector3f oldEndpoint = inputPath.get(inputPath.size()-1);
        return MeshHelper.getRotationAroundPoint(startPoint, newEndpoint, oldEndpoint);
    }

    public static void displayAngleOfRotation(Matrix4f transform) {
        Quaternion rotation = transform.toRotationQuat();
        AngleAxisRotation rot = new AngleAxisRotation(rotation);
        System.out.println("Angle is: " + rot.getAngle() + " radians");
        System.out.println("Axis of Rotation is: " + rot.getAxis());
    }
    
    public static ArrayList<Vector3f> scalePathForNewEndpoint(ArrayList<Vector3f> inputPath, Vector3f newEndpoint){
        Vector3f startPoint = inputPath.get(0);
        Vector3f oldEndpoint = inputPath.get(inputPath.size()-1);
        float currentLength = oldEndpoint.subtract(startPoint).length();
        float desiredLength = newEndpoint.subtract(startPoint).length();
        float scaleFactor = desiredLength/currentLength;
        displayScaleFactor(scaleFactor);
        Matrix4f transform = MeshHelper.getScaleAroundPoint(startPoint, scaleFactor);
        return MeshHelper.getTransformedVertices(inputPath, transform);
    }
    
    public static ArrayList<Vector3f> getCompressedPath(ArrayList<Vector3f> inputPath, float minSegmentLength){
        Vector3f lastPointAdded = inputPath.get(0);
        ArrayList<Vector3f> newPath = new ArrayList<Vector3f>(inputPath.size());
        float currentLength;
        for(Vector3f vertex: inputPath){
            currentLength = vertex.distance(lastPointAdded);
            if(currentLength > minSegmentLength){
                newPath.add(vertex);
                lastPointAdded = vertex;
            }
        }
        return newPath;
    }
    
    public static void displayScaleFactor(float scaleFactor){
        System.out.println("Scale Factor was: " + scaleFactor);
    }
    
}
