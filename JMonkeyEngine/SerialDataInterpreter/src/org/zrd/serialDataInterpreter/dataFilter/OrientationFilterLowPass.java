/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataFilter;

import org.zrd.util.stats.LowPassFilterData;

/**
 *
 * @author BLI
 */
public class OrientationFilterLowPass extends OrientationFilter{

    private LowPassFilterData yawData;
    private LowPassFilterData pitchData;
    private LowPassFilterData rollData;
    
    public OrientationFilterLowPass(float firstPitch, float firstYaw, float firstRoll){
        super(firstPitch,firstYaw,firstRoll);
        
        yawData = new LowPassFilterData(3);
        pitchData = new LowPassFilterData(3);
        rollData = new LowPassFilterData(3);
        
        yawData.addToData(firstYaw);
        pitchData.addToData(firstPitch);
        rollData.addToData(firstRoll);
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
