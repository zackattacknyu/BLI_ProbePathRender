/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.cmdapplication_testing;

import java.util.logging.Logger;

/**
 *
 * @author BLI
 */
public class CWData {
    
    //peak power in Db
    private double power;
    
    //frequency where peak power is located
    private double frequency;

    public CWData(double power, double frequency) {
        this.power = power;
        this.frequency = frequency;
    }

    public double getPower() {
        return power;
    }

    public double getFrequency() {
        return frequency;
    }
    
    
    
}
