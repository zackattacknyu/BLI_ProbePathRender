/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataFilter;

import org.zrd.util.stats.LowPassFilterData;

/**
 * * UNUSED CODE:
 * KEEP IN CASE IT WILL BE USEFUL
 *      IN THE FUTURE
 *
 * @author BLI
 */
public class OrientationFilterLowPass extends OrientationFilter{

    private LowPassFilterData yawData;
    private LowPassFilterData pitchData;
    private LowPassFilterData rollData;
    
    public OrientationFilterLowPass(){
        yawData = new LowPassFilterData(3);
        pitchData = new LowPassFilterData(3);
        rollData = new LowPassFilterData(3);
    }
    
    @Override
    public void filterData() {
        
        yawData.addToData(inputYaw);
        pitchData.addToData(inputPitch);
        rollData.addToData(inputRoll);
        
        outputPitch = yawData.getAverage();
        outputRoll = rollData.getAverage();
        outputYaw = yawData.getAverage();
    }
    
}
