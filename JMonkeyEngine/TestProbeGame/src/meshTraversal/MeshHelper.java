/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meshTraversal;

import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Triangle;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 *
 * @author Zach
 */
public class MeshHelper {
    
    public static float epsilon = (float) Math.pow(10, -8);
    public static String getTriangleInfo(Triangle triangle){
        return triangle.get(0).toString() + "," 
                + triangle.get(1).toString() + 
                "," + triangle.get(2).toString();
    }
    
    /**
     * This takes the vector starting at primarySegStart and going in the directino
     *      of primarySegEnd and figures out the scalar magnitude that is required
     *      to make it intersect the line from regSegStart to regSegEnd.
     * 
     * The line segments will be coplanar, but due to numerical error, it might
     *      not be detected as such. Thus, we will figure out the answer for the x-y
     *      coordiantes, x-z coordinates, and y-z coordinates and average the answer
     *      together. 
     * 
     * @param primarySegStart segment start point that we care about
     * @param primarySegEnd     segment end point that we care about
     * @param refSegStart       reference segment start point for segment to intersect with
     * @param regSegEnd         reference segment end point for segment to intersect with
     * @return 
     */
    public static float getLineSegmentIntersection(Vector3f primarySegStart,Vector3f primarySegEnd, Vector3f refSegStart, Vector3f refSegEnd){
        
        Vector3f primarySegVector = primarySegEnd.subtract(primarySegStart);
        Vector3f refSegVector = refSegStart.subtract(refSegEnd);
        
        Vector3f primarySegDir = primarySegVector.normalize();
        Vector3f refSegDir = refSegVector.normalize();
        
        //they are parallel
        if(primarySegDir.equals(refSegDir)){
            return Float.MAX_VALUE;
        }
        
        float primaryStartX = primarySegStart.getX();
        float primaryStartY = primarySegStart.getY();
        float primaryStartZ = primarySegStart.getZ();
        
        float primaryVecX = primarySegVector.getX();
        float primaryVecY = primarySegVector.getY();
        float primaryVecZ = primarySegVector.getZ();
        
        Vector2f primaryStartXY = new Vector2f(primaryStartX,primaryStartY);
        Vector2f primaryStartXZ = new Vector2f(primaryStartX,primaryStartZ);
        Vector2f primaryStartYZ = new Vector2f(primaryStartY,primaryStartZ);
        
        Vector2f primaryVecXY = new Vector2f(primaryVecX,primaryVecY);
        Vector2f primaryVecXZ = new Vector2f(primaryVecX,primaryVecZ);
        Vector2f primaryVecYZ = new Vector2f(primaryVecY,primaryVecZ);
        
        float refStartX = refSegStart.getX();
        float refStartY = refSegStart.getY();
        float refStartZ = refSegStart.getZ();
        
        float refVecX = refSegVector.getX();
        float refVecY = refSegVector.getY();
        float refVecZ = refSegVector.getZ();
        
        Vector2f refStartXY = new Vector2f(refStartX,refStartY);
        Vector2f refStartXZ = new Vector2f(refStartX,refStartZ);
        Vector2f refStartYZ = new Vector2f(refStartY,refStartZ);
        
        Vector2f refVecXY = new Vector2f(refVecX,refVecY);
        Vector2f refVecXZ = new Vector2f(refVecX,refVecZ);
        Vector2f refVecYZ = new Vector2f(refVecY,refVecZ);
        
        float xyResult = getLineSegmentIntersection(primaryStartXY,
                primaryVecXY,refStartXY,refVecXY);
        float xzResult = getLineSegmentIntersection(primaryStartXZ,
                primaryVecXZ,refStartXZ,refVecXZ);
        float yzResult = getLineSegmentIntersection(primaryStartYZ,
                primaryVecYZ,refStartYZ,refVecYZ);
        
        float xyzResult = 0;
        float numResults = 0;
        
        if(xyResult < Float.MAX_VALUE){
            numResults++;
            xyzResult += xyResult; 
        }
        if(yzResult < Float.MAX_VALUE){
            numResults++;
            xyzResult += yzResult; 
        }
        if(xzResult < Float.MAX_VALUE){
            numResults++;
            xyzResult += xzResult; 
        }
        
        if(numResults < 1){
            return Float.MAX_VALUE;
        }else{
            return xyzResult/numResults;
        }
        
    }
    
    /**
     * This gets the vector magnitude at the intersection when
     *      the vectors are on a 2D plane
     * @param primaryStartPt
     * @param primarySegVector
     * @param refSegStartPt
     * @param refSegVector
     * @return 
     */
    public static float getLineSegmentIntersection(Vector2f primaryStartPt, 
            Vector2f primarySegVector, Vector2f refSegStartPt, 
            Vector2f refSegVector){
        float a = primarySegVector.getX();
        float b = -1*refSegVector.getX();
        float e = refSegStartPt.getX()-primaryStartPt.getX();
        
        float c = primarySegVector.getY();
        float d = -1*refSegVector.getY();
        float f = refSegStartPt.getY()-primaryStartPt.getY();
        
        Vector2f result = solveMatrixEqu(a,b,c,d,e,f);
        if(result==null) return Float.MAX_VALUE;
        return result.getX();
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
        if(det < epsilon) return null;
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
    
}
