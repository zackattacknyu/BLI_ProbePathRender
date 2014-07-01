/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataFilter;

import org.zrd.serialDataInterpreter.dataFilter.OrientationFilter;

/**
 *
 * @author BLI
 */
public class OrientationFilterRaw extends OrientationFilter{
   
    
    public OrientationFilterRaw(float firstPitch, float firstYaw, float firstRoll){
        super(firstPitch,firstYaw,firstRoll);
    }
    
    @Override
    public void filterData() {
        outputPitch = inputPitch - firstPitch;
        outputRoll = inputRoll - firstRoll;
        outputYaw = inputYaw - firstYaw;
    }
    
}
