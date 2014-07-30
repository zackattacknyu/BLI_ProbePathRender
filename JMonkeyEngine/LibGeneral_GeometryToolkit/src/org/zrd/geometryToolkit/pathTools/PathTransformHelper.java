/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.pathTools;

import com.jme3.math.Matrix4f;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometryUtil.TransformHelper;

/**
 *
 * @author Zach
 */
public class PathTransformHelper {
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
        return TransformHelper.getRotationAroundPoint(startPoint, newEndpoint, oldEndpoint);
    }

    public static ArrayList<Vector3f> movePathStartPoint(ArrayList<Vector3f> oldPath, Vector3f targetStartPoint) {
        Vector3f startPoint = oldPath.get(0);
        Vector3f moveVector = targetStartPoint.subtract(startPoint);
        Matrix4f moveTransform = new Matrix4f();
        moveTransform.setTranslation(moveVector);
        return getTransformedVertices(oldPath, moveTransform);
    }

    /**
     * Gets a new array list consisting of the current vertices transformed
     *  using the matrix in transform
     * @param vertices  original vertices
     * @param transform matrix to use to transform the vertices
     * @return
     */
    public static ArrayList<Vector3f> getTransformedVertices(ArrayList<Vector3f> vertices, Matrix4f transform) {
        ArrayList<Vector3f> outputVertices = new ArrayList<Vector3f>(vertices.size());
        for (Vector3f vertex : vertices) {
            outputVertices.add(transform.mult(vertex));
        }
        return outputVertices;
    }
    
}
