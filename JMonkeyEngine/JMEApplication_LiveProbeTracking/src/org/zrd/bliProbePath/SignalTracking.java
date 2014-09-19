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
        if(data == null || data.length < waveformSize){
            return "empty";
        }
        double[] wave1Data = new double[waveformSize];
        //double[] wave2Data = new double[waveformSize];
        for(int i = 0; i < waveformSize; i++){
            wave1Data[i] = Double.parseDouble(data[i]);
            //wave2Data[i] = Double.parseDouble(data[i + waveformSize]);
        }
        CWData peak1Data = fftProcessor.getCWData(wave1Data);
        //CWData peak2Data = fftProcessor.getCWData(wave2Data);
        
        return String.format("Peak1Power=%.2f Peak1Freq=%.2f", 
                peak1Data.getPower(),peak1Data.getFrequency());
        
        /*return String.format("Peak1Power=%.2f Peak2Power=%.2f "
                + "Peak1Freq=%.2f Peak2Freq=%.2f", 
                peak1Data.getPower(), peak2Data.getPower(), 
                peak1Data.getFrequency(), peak2Data.getFrequency());*/
        
        
    }
    
}
