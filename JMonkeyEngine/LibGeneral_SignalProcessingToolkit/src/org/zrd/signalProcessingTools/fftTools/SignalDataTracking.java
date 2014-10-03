/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.signalProcessingTools.fftTools;

import java.util.ArrayList;
import org.zrd.util.dataHelp.DataArrayToStringConversion;
import org.zrd.util.dataHelp.SignalDataProcessor;

/**
 *
 * @author Zach
 */
public class SignalDataTracking implements DataArrayToStringConversion{
    
    private CWFFT fftProcessor;
    private SignalDataProcessor dataProcessor;
    
    public SignalDataTracking(int size, int resolution, int numWaves, int dataStart){
        fftProcessor = new CWFFT(size,resolution);
        dataProcessor = new SignalDataProcessor(numWaves,size,dataStart);
    }
    
    @Override
    public String getTextFileStringFromData(String[] data){
        ArrayList<CWData> peakData = getCWTrackingData(data);
        StringBuilder textFileString = new StringBuilder();
        for(CWData peak: peakData){
            textFileString.append(peak.getPower());
            textFileString.append(",");
            textFileString.append(peak.getFrequency());
            textFileString.append(",");
        }
        
        //removes the last comma
        textFileString.deleteCharAt(textFileString.length()-1);
        return textFileString.toString();
    }
    
    public ArrayList<CWData> getCWTrackingData(String[] data){
        ArrayList<double[]> waves = dataProcessor.getWaveformData(data);
        ArrayList<CWData> trackerData = new ArrayList<CWData>(waves.size());
        CWData peakData;
        for(double[] waveData: waves){
            peakData = fftProcessor.getCWData(waveData);
            trackerData.add(peakData);
        }
        return trackerData;
    }
    
}
