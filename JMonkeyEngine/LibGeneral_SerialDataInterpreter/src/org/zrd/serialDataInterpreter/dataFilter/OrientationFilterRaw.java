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

    @Override
    public void filterData() {
        outputPitch = inputPitch;
        outputRoll = inputRoll;
        outputYaw = inputYaw;
    }
    
}
