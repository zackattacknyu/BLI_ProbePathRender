/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.dataHelp;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Zach
 */
public class SignalDataProcessor {
    
    private int numWaveforms;
    private int waveformSize;
    private int totalNumEntries;
    private int dataStartIndex;
    
    public static final String DATA_DELIMITER = ",";

    public SignalDataProcessor(int numWaveforms, int waveformSize) {
        this.numWaveforms = numWaveforms;
        this.waveformSize = waveformSize;
        
        this.totalNumEntries = waveformSize*numWaveforms;
    }
    
    public SignalDataProcessor(int numWaveforms, int waveformSize, int dataStart) {
        this.numWaveforms = numWaveforms;
        this.waveformSize = waveformSize;
        
        this.totalNumEntries = waveformSize*numWaveforms;
        this.dataStartIndex = dataStart;
    }
    
    public ArrayList<double[]> getWaveformData(String dataString){
        String[] dataParts = getDataFromRawString(dataString);
        return getWaveformData(dataParts);
    }
    
    public ArrayList<double[]> getWaveformData(String[] dataParts){
        ArrayList<double[]> returnList = new ArrayList<double[]>(numWaveforms);
        for(int waveNum = 0; waveNum < numWaveforms; waveNum++){
            
            double[] currentWave = new double[waveformSize];
            for(int index = 0; index < waveformSize; index++){
                currentWave[index] = Double.parseDouble(dataParts[index + waveformSize*waveNum]);
            }
            returnList.add(currentWave);
        }
        return returnList;
    }
    
    public String[] getDataFromRawString(String dataString){
        return getDataFromRawString(dataString,dataStartIndex);
    }
    
    public String[] getDataFromRawString(String dataString, int dataStart){
        String[] parts = dataString.split(DATA_DELIMITER);
        return Arrays.copyOfRange(parts, dataStart, dataStart + totalNumEntries);
    }
    
}
