/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.geometricCalculations;

/**
 *
 * @author Zach
 */
public class MathHelper {

    /**
     * Tests the previous value and current value of a sequence and sees
     *      if their difference is less than the given epsilon value.
     * It thus is using Cauchy Convergence to see if a sequence has converged
     *
     * @param currentValue          current value of sequence
     * @param previousValue         previous value in sequence
     * @param epsilonValue          the operant epsilon value to use for convergence test
     * @return                      whehter or not convergence has occured
     */
    public static boolean hasSequenceConverged(float currentValue, float previousValue, float epsilonValue) {
        return Math.abs(currentValue - previousValue) < epsilonValue;
    }
    
    
    
}
