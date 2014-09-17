/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.signalProcessingTools.fftTools;

import org.zrd.signalProcessingTools.generalMath.WindowFunction;
import org.zrd.signalProcessingTools.generalMath.Complex;
import java.util.Calendar;
import org.zrd.signalProcessingTools.generalMath.PowersOf2;
import org.zrd.util.stats.IndexEntry;
import org.zrd.util.stats.StatHelper;

/**
 *
 * @author BLI
 */
public class CWFFT {
    
    public static final double CW_SAMPLING_FREQUENCY = 3.5*(Math.pow(10, 5));
    
    private int waveformSize;
    private int fftLength;
    private int fftLengthHalf;
    private double[] blackmanWindow;
    private double[] frequencies;
    
    public CWFFT(int waveformSize, int resolution){
        this.waveformSize = waveformSize;
        fftLength = PowersOf2.getPowerOf2(resolution);
        fftLengthHalf = PowersOf2.divideBy2(fftLength);
        
        //generate the blackman window
        blackmanWindow = WindowFunction.generateBlackmanWindow(waveformSize);
        
        //obtains the frequencies array
        double increment = (CW_SAMPLING_FREQUENCY/2.0)*(1.0/fftLengthHalf);
        frequencies = new double[fftLengthHalf+1];
        for(int j = 0; j <= fftLengthHalf; j++){
            frequencies[j] = j*increment;
        }
    }
    
    public CWData getCWData(double[] waveform){
        
        double meanWaveformValue = StatHelper.getMean(waveform);
        
        //shift the waveform by the mean value
        for(int i = 0; i < waveformSize; i++){
            waveform[i] = waveform[i] - meanWaveformValue;
        }
        
        //multiply waveform by blackman window to get waveform to put through FFT
        for(int i = 0; i < waveformSize; i++){
            waveform[i] = waveform[i]*blackmanWindow[i];
        }
        
        /*
         * Convert to complex numbers to do the FFT and make sure
         *      the length is correct
         */
        Complex[] windowedWaveform = new Complex[fftLength];
        for(int i = 0; i < waveformSize; i++){
            windowedWaveform[i] = new Complex(waveform[i],0);
        }
        if(fftLength > waveformSize){
            //pads the end with zeros if need be
            for(int i = waveformSize; i < fftLength; i++){
                windowedWaveform[i] = new Complex(0,0);
            }
        }
        
        //does the FFT
        long beforeFFT = Calendar.getInstance().getTimeInMillis();
        Complex[] fftData = FFT.fft(windowedWaveform);
        long afterFFT = Calendar.getInstance().getTimeInMillis();
        System.out.println("FFT Time: " + (afterFFT-beforeFFT) + " ms");
        
        //gets the power at each of the frequencies
        Double[] powerAtFreqs = new Double[fftLengthHalf];
        for(int i = 0; i < fftLengthHalf; i++){
            powerAtFreqs[i] = fftData[i].abs()/(fftLength*waveformSize);
        }
        
        //gets the peak and the frequency at that peak
        IndexEntry maxPower = StatHelper.getMax(powerAtFreqs);
        double peak = Math.log10(maxPower.getEntry())*20.0;
        double frequencyAtPeak = frequencies[maxPower.getIndex()];
        
        return new CWData(peak,frequencyAtPeak);
        
    }
}
