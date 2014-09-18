/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.bliProbePath;

import org.zrd.signalProcessingTools.fftTools.CWData;
import org.zrd.signalProcessingTools.fftTools.CWFFT;

/**
 *
 * @author BLI
 */
public class SignalTracking {
    
    private CWFFT fftProcessor;
    private int waveformSize;
    
    public SignalTracking(int size, int resolution){
        fftProcessor = new CWFFT(size,resolution);
        waveformSize = size;
    }
    
    public String getSignalTrackingInfo(String[] data){
        if(data == null || data.length < waveformSize*2){
            return "empty";
        }
        double[] wave1Data = new double[waveformSize];
        double[] wave2Data = new double[waveformSize];
        for(int i = 0; i < waveformSize; i++){
            wave1Data[i] = Double.parseDouble(data[i]);
            wave2Data[i] = Double.parseDouble(data[i + waveformSize]);
        }
        CWData peak1Data = fftProcessor.getCWData(wave1Data);
        CWData peak2Data = fftProcessor.getCWData(wave2Data);
        return " Peak1Freq: " + peak1Data.getFrequency() + 
                ", Peak1Power: " + peak1Data.getPower() +
                " Peak2Freq: " + peak2Data.getFrequency() + 
                ", Peak2Power: " + peak2Data.getPower();
        
        
    }
    
}
