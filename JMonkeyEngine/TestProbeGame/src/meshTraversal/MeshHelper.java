/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meshTraversal;

import com.jme3.math.Matrix3f;
import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Triangle;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import mygame.Constants;

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
    
    public static boolean hasNaN(Matrix4f matrix){
        
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if(Float.isNaN(matrix.get(i, j))){
                    return true;
                }
            }
        }
        return false;
        
    }
    /**
     * This solves the following matrix equation:
     *      [ a b ][s]  = [e]
     *      [ c d ][t]    [f]
     * 
     * It will return a vector in the form (s,t)
     *  and it takes in a,b,c,d,e,f
     * @param a
     * @param b
     * @param c
     * @param d
     * @param e
     * @param f
     * @return 
     */
    public static Vector2f solveMatrixEqu(float a, float b, float c, float d, float e, float f){
        float det = a*d-b*c;
        float absDet = (float)Math.abs(det);
        if(absDet < Constants.EPSILON) return null;
        float s = (d*e-b*f)/det;
        float t = (a*f-e*c)/det;
        return new Vector2f(s,t);
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
     * This gets the matrix transformation for a scaling around an origin point
     * @param originPoint
     * @param rotation
     * @return 
     */
    public static Matrix4f getScaleAroundPoint(Vector3f originPoint, float scaleFactor){
        Matrix4f firstTranslation = new Matrix4f();
        Matrix4f lastTranslation = new Matrix4f();
        
        firstTranslation.setTranslation(originPoint);
        Vector3f startingPtNeg = originPoint.clone().negate();
        lastTranslation.setTranslation(startingPtNeg);
        
        Matrix4f scaleTransform = new Matrix4f();
        scaleTransform.setScale(scaleFactor, scaleFactor, scaleFactor);
        return (firstTranslation.mult(scaleTransform)).mult(lastTranslation);
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
    
    /**
     * This gets us the whole transformation for moving a point onto a plane
     *      using the projection of the vector
     * @param normal        normal to the plane
     * @param originPoint   origin to rotate around
     * @param actualEndPt   end point to move onto plane
     * @return 
     */
    public static Matrix4f getRotationOntoPlane(Vector3f normal, Vector3f originPoint, Vector3f actualEndPt){
        Vector3f actualDir = getDirectionVector(originPoint,actualEndPt);
        Quaternion rotation = getRotationOntoPlane(normal,actualDir);
        return getRotationAroundPoint(originPoint,rotation);
    }
    
    /**
     * Gets the rotation quaternion for rotating a vector
     *      so that it is on the plane described by the normal vector
     * @param normal        the normal to the plane
     * @param actualDir     the vector to rotate to be on the normal
     * @return 
     */
    public static Quaternion getRotationOntoPlane(Vector3f normal, Vector3f actualDir){
        Vector3f targetDir = getVectorProjOnPlane(normal, actualDir);
        return getRotationFromVectors(actualDir,targetDir);
    }
    
    /**
     * This gets the target vector that is the projection of the startVector
     *      onto the plane
     * @param normal            the normal vector
     * @param startVector       the vector to get the projection for
     * @return 
     */
    public static Vector3f getVectorProjOnPlane(Vector3f normal, Vector3f startVector){
        Vector3f newNorm = normal.normalize();
        Vector3f newStartVec = startVector.normalize();
        float dotProd = newNorm.dot(newStartVec);
        Vector3f diffVector = newNorm.mult(dotProd);
        return newStartVec.subtract(diffVector);
        
    }
    
    /**
     * This obtains the matrix transformation that translates
     *      points so that the given point will be the new origin
     *      point in the new coordinate system. 
     * @param newOrigin
     * @return 
     */
    public static Matrix4f makeNewOrigin(Vector3f newOrigin){
        Matrix4f originVertex1 = new Matrix4f();
        originVertex1.setTranslation(newOrigin.clone().negate());
        return originVertex1;
    }
    
    /**
     * This finds the coordinate transformation from the standard basis
     *      to these three vectors as the basis. 
     * @param vector1
     * @param vector2
     * @param vector3
     * @return 
     */
    public static Matrix3f getCoordinateTransformation(Vector3f vector1,Vector3f vector2, Vector3f vector3){
        Matrix3f coordMatrix = new Matrix3f();
       float[][] coords = new float[3][3];
       for(int j = 0; j < 3; j++){
            coords[j][0] = vector1.get(j);
            coords[j][1] = vector2.get(j);
            coords[j][2] = vector3.get(j);
        }
       coordMatrix.set(coords);
       coordMatrix.invertLocal();
       return coordMatrix;
    }
    
}
