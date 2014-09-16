/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.cmdapplication_testing;

import java.util.Arrays;
import org.zrd.util.stats.StatHelper;

/**
 *
 * @author BLI
 */
public class CWFFT {
    
    public static final double CW_SAMPLING_FREQUENCY = Math.pow(10, -5);
    
    public static CWData getCWFFTData(Double[] waveform, int resolution){
        
        int waveformSize = waveform.length;
        
        double meanWaveformValue = StatHelper.getMean(waveform);
        
        //shift the waveform by the mean value
        for(int i = 0; i < waveformSize; i++){
            waveform[i] = waveform[i] - meanWaveformValue;
        }
        
        //generate the blackman window
        double[] blackmanWindow = WindowFunction.generateBlackmanWindow(waveformSize);
        
        //multiply waveform by blackman window to get waveform to put through FFT
        double[] modifiedWaveform = new double[waveformSize];
        for(int i = 0; i < waveformSize; i++){
            modifiedWaveform[i] = waveform[i]*blackmanWindow[i];
        }
        
        //convert them to Complex to do FFT operation
        Complex[] windowedWaveform = new Complex[waveformSize];
        for(int i = 0; i < waveformSize; i++){
            windowedWaveform[i] = new Complex(modifiedWaveform[i],0);
        }
        
        int fftLength = (int)Math.pow(2, resolution);
        
        /*
         * Gets the waveform padded or truncated to match
         *      the FFT length
         */
        Complex[] windowedWaveformAdj = Arrays.copyOf(windowedWaveform, fftLength);
        if(fftLength > waveformSize){
            //pads the end with zeros if need be
            for(int i = waveformSize; i < fftLength; i++){
                windowedWaveformAdj[i] = new Complex(0,0);
            }
        }
        
        //does the FFT
        Complex[] fftData = FFT.fft(windowedWaveformAdj);
        
        //divides the results by the size
        Complex waveformDataSize = new Complex(waveformSize,0);
        for(int i = 0; i < fftLength; i++){
            fftData[i] = fftData[i].divides(waveformDataSize);
        }
        
        int fftLengthUse = (int)(Math.pow(2, resolution-1));
        double[] frequencies = new double[fftLengthUse+1];
        
    }
}
