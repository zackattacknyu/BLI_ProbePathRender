/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.dataHelp;

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
    
    public String[] getDataFromRawString(String dataString){
        return getDataFromRawString(dataString,dataStartIndex);
    }
    
    public String[] getDataFromRawString(String dataString, int dataStart){
        String[] parts = dataString.split(DATA_DELIMITER);
        return Arrays.copyOfRange(parts, dataStart, dataStart + totalNumEntries);
    }
    
}
