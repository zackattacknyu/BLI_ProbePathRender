/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.recordedPathRendering;

import com.jme3.math.ColorRGBA;
import org.zrd.jmeGeometryIO.pathIO.StringToColorConversion;
import org.zrd.signalProcessingTools.fftTools.CWData;
import org.zrd.signalProcessingTools.fftTools.CWFFT;

/**
 *
 * @author BLI
 */
public class SignalProcess implements StringToColorConversion{
    
    private CWFFT fftProcessor;
    private int waveformSize;
    
    public SignalProcess(int size, int resolution){
        fftProcessor = new CWFFT(size,resolution);
        waveformSize = size;
    }
    
    public double getWavePeak(String[] data){
        //if(data == null || data.length < waveformSize*2){
        if(data == null || data.length < waveformSize){
            //System.out.println("Data Array Length = " + data.length);
            return 0;
        }
        double[] wave1Data = new double[waveformSize];
        //double[] wave2Data = new double[waveformSize];
        for(int i = 0; i < waveformSize; i++){
            wave1Data[i] = Double.parseDouble(data[i]);
            //wave2Data[i] = Double.parseDouble(data[i + waveformSize]);
        }
        CWData peak1Data = fftProcessor.getCWData(wave1Data);
        //CWData peak2Data = fftProcessor.getCWData(wave2Data);
        
        return peak1Data.getPower();
        
        
    }

    public ColorRGBA convertStringToColor(String[] data) {
        double dataPeak = getWavePeak(data);
        
        float brightness = (float)((dataPeak+87)/1.8);
        
        System.out.println("Brightness: " + brightness);
        return new ColorRGBA(1-brightness,0f,brightness,1.0f);
    }
    
}
