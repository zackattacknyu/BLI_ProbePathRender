/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.bliProbePath;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zrd.signalProcessingTools.fftTools.CWData;
import org.zrd.signalProcessingTools.fftTools.SignalDataTracking;

/**
 *
 * @author BLI
 */
public class SignalTracking_BLIProbePath_Threaded implements Runnable{
    
    private SignalDataTracking dataTracker;

    private String[] data;
    private String currentOutput;
    
    public SignalDataTracking getDataTracker() {
        return dataTracker;
    }
    
    public SignalTracking_BLIProbePath_Threaded(int size, int resolution){
        int numWaves = 2;
        int indexStart = 0;
        dataTracker = new SignalDataTracking(size,resolution,numWaves,indexStart);
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public String getCurrentOutput() {
        return currentOutput;
    }
    
    private void getSignalTrackingInfo(){
        
        if(data == null) return;
        
        StringBuilder retString = new StringBuilder();
        ArrayList<CWData> signalData = dataTracker.getCWTrackingData(data);
        for(CWData currentData: signalData){
            retString.append(String.format("PeakPower=%.2f PeakFreq=%.2f ; ",
                currentData.getPower(), currentData.getFrequency()));
        }
        currentOutput = retString.toString();
        
    }

    public void run() {
        
        while(true){
            
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
            getSignalTrackingInfo();
        
        }
        
    }
    
}
