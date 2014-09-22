/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.bliProbePath;

import java.util.ArrayList;
import org.zrd.signalProcessingTools.fftTools.CWData;
import org.zrd.signalProcessingTools.fftTools.SignalDataTracking;

/**
 *
 * @author BLI
 */
public class SignalTracking_BLIProbePath {
    
    private SignalDataTracking dataTracker;

    public SignalDataTracking getDataTracker() {
        return dataTracker;
    }
    
    public SignalTracking_BLIProbePath(int size, int resolution){
        int numWaves = 2;
        int indexStart = 0;
        dataTracker = new SignalDataTracking(size,resolution,numWaves,indexStart);
    }
    
    public String getSignalTrackingInfo(String[] data){
        
        StringBuilder retString = new StringBuilder();
        ArrayList<CWData> signalData = dataTracker.getCWTrackingData(data);
        for(CWData currentData: signalData){
            retString.append(String.format("PeakPower=%.2f PeakFreq=%.2f ; ",
                currentData.getPower(), currentData.getFrequency()));
        }
        
        return retString.toString();
        
        
    }
    
}
