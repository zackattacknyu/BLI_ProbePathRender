/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.cmdapplication_testing;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author BLI
 */
public class CMDApplication_Testing {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        /*int n = 16;
        FFT.testFFT(n);
        double[] blackmanWindow = WindowFunction.generateBlackmanWindow(n);
        for(double windowVal: blackmanWindow){
            System.out.print(windowVal + " ");
        }*/

        ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(Paths.get("input.txt"),StandardCharsets.US_ASCII);
        String line1 = lines.get(0);
        String[] vals = line1.split(",");
        double[] waveform1 = new double[vals.length];
        for(int i = 0; i < vals.length; i++){
            waveform1[i] = Double.parseDouble(vals[i]);
        }
        
        long beforeCalc = Calendar.getInstance().getTimeInMillis();
        CWData data = CWFFT.getCWFFTData(waveform1, 14);
        long afterCalc = Calendar.getInstance().getTimeInMillis();
        
        System.out.println("Peak Power: " + data.getPower());
        System.out.println("Frequency at Peak: " + data.getFrequency());
        System.out.println("Time For Calculation: " + (afterCalc-beforeCalc) + " ms");
        
    }

}
