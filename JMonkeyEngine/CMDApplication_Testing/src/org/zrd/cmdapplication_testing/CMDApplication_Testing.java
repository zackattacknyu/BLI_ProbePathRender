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

        long beforeInit = Calendar.getInstance().getTimeInMillis();
        CWFFT currentSession = new CWFFT(50,14);
        long afterInit = Calendar.getInstance().getTimeInMillis();
        System.out.println("Time for Init: " + (afterInit-beforeInit) + " ms");
        
        processData("input1.txt",currentSession);
        processData("input2.txt",currentSession);
        processData("input3.txt",currentSession);
        
    }
    
    public static void processData(String textFile,CWFFT currentSession) throws IOException{
        long beforeCalc = Calendar.getInstance().getTimeInMillis();
        CWData data = currentSession.getCWData(getWaveform(textFile));
        long afterCalc = Calendar.getInstance().getTimeInMillis();
        
        System.out.println("Peak Power: " + data.getPower());
        System.out.println("Frequency at Peak: " + data.getFrequency());
        System.out.println("Time For Calculation: " + (afterCalc-beforeCalc) + " ms");
    }
    
    public static double[] getWaveform(String textFile) throws IOException{
        ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(Paths.get(textFile),StandardCharsets.US_ASCII);
        String line1 = lines.get(0);
        String[] vals = line1.split(",");
        double[] waveform1 = new double[vals.length];
        for(int i = 0; i < vals.length; i++){
            waveform1[i] = Double.parseDouble(vals[i]);
        }
        return waveform1;
    }
    

}
