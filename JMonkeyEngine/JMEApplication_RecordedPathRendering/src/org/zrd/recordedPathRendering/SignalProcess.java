/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.recordedPathRendering;

import com.jme3.math.ColorRGBA;
import java.util.ArrayList;
import org.zrd.jmeGeometryIO.pathIO.StringToColorConversion;
import org.zrd.signalProcessingTools.fftTools.CWData;
import org.zrd.signalProcessingTools.fftTools.CWFFT;
import org.zrd.signalProcessingTools.fftTools.SignalDataTracking;

/**
 *
 * @author BLI
 */
public class SignalProcess implements StringToColorConversion{
    
    private SignalDataTracking dataTracker;
    
    //true is red. black is false
    private boolean redBlackSwitch = true;
    
    public SignalProcess(int size, int resolution){
        int numWaves = 2;
        int indexStart = 0;
        dataTracker = new SignalDataTracking(size,resolution,numWaves,indexStart);
    }
    
    public double getWavePeak(String[] data){
        
        /*
         * CODE IF DATA CONTAINS THE WAVEFORM DATA
        ArrayList<CWData> signalData = dataTracker.getCWTrackingData(data);
        return signalData.get(1).getPower();
        */
        
        /*
         * CODE IF DATA ALREADY CONTAINS PEAK
         */
        //return Double.parseDouble(data[2]);
        
        /*
         * CODE TO NOT DISPLAY ANY DATA
         */
        //return 0;
        
        if(data[2] == null){
            return 0;
        }else{
            return Double.parseDouble(data[2]);
        }
    }

    public ColorRGBA convertStringToColor2(String[] data){
        return ColorRGBA.Black;
    }
    public ColorRGBA convertStringToColor(String[] data) {
        float lowThreshold = 52;
        float highThreshold = 57;
        
        double dataPeak = getWavePeak(data);
        //System.out.println("Peak=" + dataPeak);
        float brightness = (float)(dataPeak*-1);

        /*
        if(brightness < 45){
            brightness = 1;
        }else if(brightness > 55){
            brightness = 0;
        }else{
            brightness = 1 - (brightness-45)/10;
        }
        */
        
        if(brightness < lowThreshold){
            brightness = 1;
        }else if(brightness > highThreshold){
            brightness = 0;
        }else{
            brightness = 1 - (brightness-lowThreshold)/(highThreshold-lowThreshold);
        }
        
        //System.out.println("Brightness: " + brightness);
        return new ColorRGBA(1-brightness,0f,brightness,1.0f);

        /*if(redBlackSwitch){
            return ColorRGBA.Red;
        }else{
            return ColorRGBA.Black;
        }*/
    }

    public void switchRedBlack() {
        redBlackSwitch = !redBlackSwitch;
    }
    
    
    
}
