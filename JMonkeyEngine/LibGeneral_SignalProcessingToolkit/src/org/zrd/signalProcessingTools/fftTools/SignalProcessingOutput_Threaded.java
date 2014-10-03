/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.signalProcessingTools.fftTools;

import java.util.ArrayList;
import org.zrd.util.dataStreaming.ThreadedOutput;

/**
 *
 * @author BLI
 */
public class SignalProcessingOutput_Threaded implements ThreadedOutput{
    
    private SignalDataTracking dataTracker;

    private String[] data;
    private String currentOutput;
    private boolean displayOutput = false;
    
    public SignalDataTracking getDataTracker() {
        return dataTracker;
    }
    
    public SignalProcessingOutput_Threaded(int size, int resolution){
        int numWaves = 2;
        int indexStart = 0;
        dataTracker = new SignalDataTracking(size,resolution,numWaves,indexStart);
    }

    public void setDisplayOutput(boolean displayOutput) {
        this.displayOutput = displayOutput;
    }

    @Override
    public void setData(String[] data) {
        this.data = data;
    }

    @Override
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
        
        if(displayOutput){
            System.out.println(currentOutput);
        }
    }

    @Override
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
