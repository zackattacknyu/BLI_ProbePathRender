/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking.probeTrackers;

import java.util.Properties;
import org.zrd.serialDataInterpreter.dataInterpretation.SerialDataInterpreter;

/**
 *
 * @author BLI
 */
public class SerialInputSourceTracker implements AbstractInputSourceTracker{
    
    SerialDataInterpreter dataInterpreter;
    
    public SerialInputSourceTracker(Properties trackerProps){
        dataInterpreter = new SerialDataInterpreter(trackerProps);
    }
    
    public void updateData(){
        dataInterpreter.updateData();
    }

    public float getCurrentYawRadians() {
        return dataInterpreter.getOutputYawRadians();
    }

    public float getCurrentPitchRadians() {
        return dataInterpreter.getOutputPitchRadians();
    }

    public float getCurrentRollRadians() {
        return dataInterpreter.getOutputRollRadians();
    }

    public float getDeltaX() {
        return dataInterpreter.getDeltaX();
    }

    public float getDeltaY() {
        return dataInterpreter.getDeltaY();
    }
    
}
