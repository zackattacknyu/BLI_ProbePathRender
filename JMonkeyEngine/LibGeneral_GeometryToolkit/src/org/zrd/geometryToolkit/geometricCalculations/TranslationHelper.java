/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.geometricCalculations;

import com.jme3.math.Matrix4f;
import com.jme3.math.Vector3f;

/**
 *
 * @author BLI
 */
public class TranslationHelper {

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

    /**
     * This gets the vector that goes from the start point to the end point
     *      and is normalized
     * @param originPoint   origin of vector
     * @param endPoint      end point of vector
     * @return              normalized vector from origin to end
     */
    public static Vector3f getUnitDirectionVector(Vector3f originPoint, Vector3f endPoint) {
        Vector3f direction = endPoint.clone().subtract(originPoint);
        return direction.normalize();
    }
    
}
