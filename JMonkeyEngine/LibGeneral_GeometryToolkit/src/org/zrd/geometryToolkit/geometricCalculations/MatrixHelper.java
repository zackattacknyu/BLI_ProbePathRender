/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.geometricCalculations;

import com.jme3.math.Vector2f;
import org.zrd.geometryToolkit.geometryUtil.GeometryToolkitConstants;

/**
 *
 * @author Zach
 */
public class MatrixHelper {

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
    public static Vector2f solveMatrixEqu(float a, float b, float c, float d, float e, float f) {
        float det = a * d - b * c;
        float absDet = (float) Math.abs(det);
        if (absDet < GeometryToolkitConstants.EPSILON) {
            return null;
        }
        float s = (d * e - b * f) / det;
        float t = (a * f - e * c) / det;
        return new Vector2f(s, t);
    }
    
}
