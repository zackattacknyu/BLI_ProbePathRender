/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataFilter;

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
        outputPitch = inputPitch;
        outputRoll = inputRoll;
        outputYaw = inputYaw;
    }
    
}
