/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.geometricCalculations;

import com.jme3.math.Matrix3f;
import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author Zach
 */
public class TransformHelper {

    /**
     * This gets the vector that is the input vector rotated onto the plane
     *      described by the normal.
     * The difference between this method and getUnitVectorProjectionOntoPlane is that
     *      this one preserves the length of the vector
     * @param normal        normal to the plane
     * @param vector        vector to rotate onto the plane
     * @return              the vector rotated onto the plane with length preserved
     */
    public static Vector3f getVectorProjectedOntoPlane(Vector3f normal, Vector3f vector) {
        Vector3f unitVectorOnPlane = getUnitVectorProjectionOntoPlane(normal, vector);
        float length = vector.length();
        return unitVectorOnPlane.mult(length);
    }

    public static Vector3f getDirectionVector(Vector3f originPoint, Vector3f endPoint) {
        Vector3f direction = endPoint.clone().subtract(originPoint);
        return direction.normalize();
    }

    /**
     * This gets the matrix transformation for a rotation around an origin point
     * @param originPoint
     * @param rotation
     * @return
     */
    public static Matrix4f getRotationAroundPoint(Vector3f originPoint, Quaternion rotation) {
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
    public static Matrix4f getRotationAroundPoint(Vector3f originPoint, Vector3f expectedEndPt, Vector3f actualEndPt) {
        Vector3f expectedDir = getDirectionVector(originPoint, expectedEndPt);
        Vector3f actualDir = getDirectionVector(originPoint, actualEndPt);
        Quaternion rotQuaternion = getRotationFromVectors(actualDir, expectedDir);
        return TransformHelper.getRotationAroundPoint(originPoint, rotQuaternion);
    }

    /**
     * This finds the coordinate transformation from the standard basis
     *      to these three vectors as the basis.
     * @param vector1
     * @param vector2
     * @param vector3
     * @return
     */
    public static Matrix3f getCoordinateTransformation(Vector3f vector1, Vector3f vector2, Vector3f vector3) {
        Matrix3f coordMatrix = new Matrix3f();
        float[][] coords = new float[3][3];
        for (int j = 0; j < 3; j++) {
            coords[j][0] = vector1.get(j);
            coords[j][1] = vector2.get(j);
            coords[j][2] = vector3.get(j);
        }
        coordMatrix.set(coords);
        coordMatrix.invertLocal();
        return coordMatrix;
    }

    /**
     * Gets the rotation quaternion for rotating a vector
     *      so that it is on the plane described by the normal vector
     *
     * @param normal            normal to the plane
     * @param originPoint       start point of vector to rotate
     * @param actualEndPt       end point of vector to rotate
     * @return
     */
    public static Quaternion getRotationOntoPlaneQuat(Vector3f normal, Vector3f originPoint, Vector3f actualEndPt) {
        Vector3f actualDir = getDirectionVector(originPoint, actualEndPt);
        return getRotationOntoPlane(normal, actualDir);
    }

    /**
     * This gets the matrix transformation for a scaling around an origin point
     * @param originPoint
     * @param rotation
     * @return
     */
    public static Matrix4f getScaleAroundPoint(Vector3f originPoint, float scaleFactor) {
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
     * Gets the rotation quaternion for rotating a vector
     *      so that it is on the plane described by the normal vector
     * @param normal        the normal to the plane
     * @param actualDir     the vector to rotate to be on the normal
     * @return
     */
    public static Quaternion getRotationOntoPlane(Vector3f normal, Vector3f actualDir) {
        Vector3f targetDir = getUnitVectorProjectionOntoPlane(normal, actualDir);
        return getRotationFromVectors(actualDir, targetDir);
    }

    /**
     * This gets the target vector that is the projection of the startVector
     *      onto the plane described by the normal.
     * If N is the normal, S is the start vector (both unit vectors),
     *      then the target T is obtained using this procedure:
     * 1) Calculate dotProd as the dot product of N and S
     * 2) Let D be the projection of vector S onto N. D = N*dotProd
     * 3) We know that T is a projection of S onto the plane
     *      thus T + D = S, hence T = S - D
     * @param normal            the normal vector
     * @param startVector       the vector to get the projection for
     * @return      the unit vector that is the start vector on the plane
     */
    public static Vector3f getUnitVectorProjectionOntoPlane(Vector3f normal, Vector3f startVector) {
        Vector3f newNorm = normal.normalize();
        Vector3f newStartVec = startVector.normalize();
        float dotProd = newNorm.dot(newStartVec);
        Vector3f diffVector = newNorm.mult(dotProd);
        return newStartVec.subtract(diffVector);
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
    public static Quaternion getRotationFromVectors(Vector3f actualDir, Vector3f desiredDir) {
        float cosTheta = desiredDir.dot(actualDir);
        float rotAngle = (float) Math.acos(cosTheta);
        Vector3f rotAxis = desiredDir.cross(actualDir);
        rotAxis = rotAxis.normalize();
        Quaternion rotQuaternion = new Quaternion();
        rotQuaternion.fromAngleAxis(-1 * rotAngle, rotAxis);
        return rotQuaternion;
    }

    /**
     * This obtains the matrix transformation that translates
     *      points so that the given point will be the new origin
     *      point in the new coordinate system.
     * @param newOrigin
     * @return
     */
    public static Matrix4f getNewOriginTransform(Vector3f newOrigin) {
        Matrix4f originVertex1 = new Matrix4f();
        originVertex1.setTranslation(newOrigin.clone().negate());
        return originVertex1;
    }
    
}
