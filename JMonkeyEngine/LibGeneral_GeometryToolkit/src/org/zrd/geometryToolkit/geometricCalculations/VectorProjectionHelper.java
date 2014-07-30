/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.geometricCalculations;

import com.jme3.math.Vector3f;

/**
 *
 * @author BLI
 */
public class VectorProjectionHelper {

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
    
}
