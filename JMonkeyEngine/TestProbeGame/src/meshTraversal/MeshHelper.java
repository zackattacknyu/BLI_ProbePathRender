/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meshTraversal;

import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 *
 * @author Zach
 */
public class MeshHelper {
    
    
    public static String getTriangleInfo(Triangle triangle){
        return triangle.get(0).toString() + "," 
                + triangle.get(1).toString() + 
                "," + triangle.get(2).toString();
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
    public static Quaternion getRotationFromVectors(Vector3f actualDir, Vector3f desiredDir){
        float cosTheta = desiredDir.dot(actualDir);
        float rotAngle = (float)Math.acos(cosTheta);
        
        Vector3f rotAxis = desiredDir.cross(actualDir);
        rotAxis = rotAxis.normalize();
        
        Quaternion rotQuaternion = new Quaternion();
        
        //the -1 is necessary to ensure the actual vector rotates to the expected vector
        rotQuaternion.fromAngleAxis(-1*rotAngle, rotAxis);
        
        return rotQuaternion;
        
    }
    
    public static Vector3f getDirectionVector(Vector3f originPoint, Vector3f endPoint){
        Vector3f direction = endPoint.clone().subtract(originPoint);
        return direction.normalize();
    }
    
    /**
     * This gets the matrix transformation for a rotation around an origin point
     * @param originPoint
     * @param rotation
     * @return 
     */
    public static Matrix4f getRotationAroundPoint(Vector3f originPoint, Quaternion rotation){
        Matrix4f firstTranslation = new Matrix4f();
        Matrix4f lastTranslation = new Matrix4f();
        
        firstTranslation.setTranslation(originPoint);
        Vector3f startingPtNeg = originPoint.clone().negate();
        lastTranslation.setTranslation(startingPtNeg);
        
        Matrix4f rotTransform = new Matrix4f();
        rotTransform.setRotationQuaternion(rotation);
        return (firstTranslation.mult(rotTransform)).mult(lastTranslation);
    }
    
    /**
     * Gets the transformation for a rotation to go from actual end point to expected end point
     * @param originPoint       point to rotate around
     * @param expectedEndPt     desired end point
     * @param actualEndPt       actual end point
     * @return 
     */
    public static Matrix4f getRotationAroundPoint(Vector3f originPoint, Vector3f expectedEndPt, Vector3f actualEndPt){
        Vector3f expectedDir = getDirectionVector(originPoint, expectedEndPt);
        Vector3f actualDir = getDirectionVector(originPoint, actualEndPt);
        Quaternion rotQuaternion = getRotationFromVectors(actualDir, expectedDir);
        return MeshHelper.getRotationAroundPoint(originPoint, rotQuaternion);
    }
    
    /**
     * Gets a new array list consisting of the current vertices transformed
     *  using the matrix in transform
     * @param vertices  original vertices
     * @param transform matrix to use to transform the vertices
     * @return 
     */
    public static ArrayList<Vector3f> getTransformedVertices(ArrayList<Vector3f> vertices, Matrix4f transform){
        ArrayList<Vector3f> outputVertices = new ArrayList<Vector3f>(vertices.size());
        for(Vector3f vertex:vertices){
            outputVertices.add(transform.mult(vertex));
        }
        return outputVertices;
    }
    
}
