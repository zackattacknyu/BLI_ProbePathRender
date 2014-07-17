/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.client;

import org.zrd.util.trackingInterface.AbstractInputSourceTracker;
import java.util.Properties;
import org.zrd.serialDataInterpreter.dataInterpretation.SerialDataInterpreter;

/**
 *
 * @author BLI
 */
public class SerialInputSourceTracker implements AbstractInputSourceTracker{
    
    SerialDataInterpreter dataInterpreter;
    private int mode = 0;
    
    public SerialInputSourceTracker(Properties trackerProps){
        dataInterpreter = new SerialDataInterpreter(trackerProps);
    }
    
    @Override
    public void updateData(){
        dataInterpreter.updateData();
    }

    @Override
    public float getCurrentYawRadians() {
        return dataInterpreter.getOutputYawRadians();
    }

    @Override
    public float getCurrentPitchRadians() {
        return dataInterpreter.getOutputPitchRadians();
    }

    @Override
    public float getCurrentRollRadians() {
        return dataInterpreter.getOutputRollRadians();
    }

    @Override
    public float getDeltaX() {
        return dataInterpreter.getDeltaX();
    }

    @Override
    public float getDeltaY() {
        return dataInterpreter.getDeltaY();
    }

    @Override
    public void resetProbeReader() {
        dataInterpreter.getReader().getSerialInterface().reset();
    }
    
}
