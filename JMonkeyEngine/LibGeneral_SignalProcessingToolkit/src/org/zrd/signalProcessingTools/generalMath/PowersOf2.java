/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.signalProcessingTools.generalMath;

/**
 *
 * @author Zach
 */
public class PowersOf2 {

    public static int getPowerOf2(int exponent) {
        return 1 << exponent;
    }

    public static int divideBy2(int divisor) {
        return divisor >> 1;
    }
    
}
