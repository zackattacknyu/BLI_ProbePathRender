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
public class ScaleTransformHelper {

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
    
}
