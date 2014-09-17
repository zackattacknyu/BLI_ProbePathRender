/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.cmdapplication_testing;

import java.util.Arrays;
import java.util.Calendar;
import org.zrd.util.stats.IndexEntry;
import org.zrd.util.stats.StatHelper;

/**
 *
 * @author BLI
 */
public class CWFFT {
    
    public static final double CW_SAMPLING_FREQUENCY = 3.5*(Math.pow(10, 5));
    
    public static int getPowerOf2(int exponent){
        return (1 << exponent);
    }
    
    public static int divideBy2(int divisor){
        return divisor >> 1;
    }
    
    public static CWData getCWFFTData(double[] waveform, int resolution){
        
        int waveformSize = waveform.length;
        int fftLength = getPowerOf2(resolution);
        int fftLengthToUse = divideBy2(fftLength);
        
        double meanWaveformValue = StatHelper.getMean(waveform);
        
        //shift the waveform by the mean value
        for(int i = 0; i < waveformSize; i++){
            waveform[i] = waveform[i] - meanWaveformValue;
        }
        
        //generate the blackman window
        double[] blackmanWindow = WindowFunction.generateBlackmanWindow(waveformSize);
        
        //multiply waveform by blackman window to get waveform to put through FFT
        for(int i = 0; i < waveformSize; i++){
            waveform[i] = waveform[i]*blackmanWindow[i];
        }
        
        //convert them to Complex to do FFT operation
        Complex[] windowedWaveform = new Complex[waveformSize];
        for(int i = 0; i < waveformSize; i++){
            windowedWaveform[i] = new Complex(waveform[i],0);
        }
        
        
        
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
        //long beforeFFT = Calendar.getInstance().getTimeInMillis();
        Complex[] fftData = FFT.fft(windowedWaveformAdj);
        //long afterFFT = Calendar.getInstance().getTimeInMillis();
        //System.out.println("Time for FFT: " + (afterFFT-beforeFFT) + " ms");
        
        //divides the results by the size
        Complex waveformDataSize = new Complex(waveformSize,0);
        for(int i = 0; i < fftLength; i++){
            fftData[i] = fftData[i].divides(waveformDataSize);
        }
        
        //obtains the frequencies array
        
        double increment = (CW_SAMPLING_FREQUENCY/2.0)*(1.0/fftLengthToUse);
        double[] frequencies = new double[fftLengthToUse+1];
        for(int j = 0; j <= fftLengthToUse; j++){
            frequencies[j] = j*increment;
        }
        
        //gets the power at each of the frequencies
        Double[] powerAtFreqs = new Double[fftLengthToUse];
        for(int i = 0; i < fftLengthToUse; i++){
            powerAtFreqs[i] = fftData[i].abs()/fftLength;
        }
        
        //gets the peak and the frequency at that peak
        IndexEntry maxPower = StatHelper.getMax(powerAtFreqs);
        double peak = Math.log10(maxPower.getEntry())*20.0;
        double frequencyAtPeak = frequencies[maxPower.getIndex()];
        
        return new CWData(peak,frequencyAtPeak);
        
    }
}
