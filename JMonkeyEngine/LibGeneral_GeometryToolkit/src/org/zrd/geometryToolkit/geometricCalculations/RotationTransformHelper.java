/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.geometricCalculations;

import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author BLI
 */
public class RotationTransformHelper {

    /**
     * Gets the transformation for a rotation to go from actual end point to expected end point
     * @param originPoint       point to rotate around
     * @param expectedEndPt     desired end point
     * @param actualEndPt       actual end point
     * @return
     */
    public static Matrix4f getRotationAroundPoint(Vector3f originPoint, Vector3f expectedEndPt, Vector3f actualEndPt) {
        Vector3f expectedDir = TranslationHelper.getUnitDirectionVector(originPoint, expectedEndPt);
        Vector3f actualDir = TranslationHelper.getUnitDirectionVector(originPoint, actualEndPt);
        Quaternion rotQuaternion = getRotationFromVectors(actualDir, expectedDir);
        return RotationTransformHelper.getRotationAroundPoint(originPoint, rotQuaternion);
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
     * Gets the rotation quaternion for rotating a vector
     *      so that it is on the plane described by the normal vector
     * @param normal        the normal to the plane
     * @param actualDir     the vector to rotate to be on the normal
     * @return
     */
    public static Quaternion getRotationOntoPlane(Vector3f normal, Vector3f actualDir) {
        Vector3f targetDir = VectorProjectionHelper.getUnitVectorProjectionOntoPlane(normal, actualDir);
        return getRotationFromVectors(actualDir, targetDir);
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
    public static Quaternion getRotationOntoPlane(Vector3f normal, Vector3f originPoint, Vector3f actualEndPt) {
        Vector3f actualDir = TranslationHelper.getUnitDirectionVector(originPoint, actualEndPt);
        return getRotationOntoPlane(normal, actualDir);
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
    
}
