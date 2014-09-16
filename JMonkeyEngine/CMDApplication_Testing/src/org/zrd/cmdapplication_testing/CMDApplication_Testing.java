/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.cmdapplication_testing;

import java.util.Calendar;

/**
 *
 * @author BLI
 */
public class CMDApplication_Testing {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        /*int n = 16;
        FFT.testFFT(n);
        double[] blackmanWindow = WindowFunction.generateBlackmanWindow(n);
        for(double windowVal: blackmanWindow){
            System.out.print(windowVal + " ");
        }*/
        
        //data at index 4 in the CW data
        double[] waveform1 = {2004,1944,1889,1838,1806,1793,1801,1823,1865,1919,
            1979,2034,2070,2096,2098,2083,2049,2004,1949,1894,1847,1816,1799,1803,
            1826,1869,1922,1976,2024,2064,2088,2094,2081,2049,2006,1955,1902,1857,
            1819,1797,1805,1827,1862,1912,1965,2017,2058,2083,2095,2082,1963,
            2027,2084,2147,2200,2237
            };
        
        long beforeCalc = Calendar.getInstance().getTimeInMillis();
        CWData data = CWFFT.getCWFFTData(waveform1, 14);
        long afterCalc = Calendar.getInstance().getTimeInMillis();
        
        System.out.println("Peak Power: " + data.getPower());
        System.out.println("Frequency at Peak: " + data.getFrequency());
        System.out.println("Time For Calculation: " + (afterCalc-beforeCalc) + " ms");
        
    }

}
