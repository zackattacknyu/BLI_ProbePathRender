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
    
    public SignalProcess(int size, int resolution){
        int numWaves = 2;
        int indexStart = 0;
        dataTracker = new SignalDataTracking(size,resolution,numWaves,indexStart);
    }
    
    public double getWavePeak(String[] data){
        ArrayList<CWData> signalData = dataTracker.getCWTrackingData(data);
        return signalData.get(1).getPower();
        
    }

    public ColorRGBA convertStringToColor(String[] data) {
        double dataPeak = getWavePeak(data);
        System.out.println("Peak=" + dataPeak);
        float brightness = (float)((dataPeak+87)/1.8);
        
        //System.out.println("Brightness: " + brightness);
        return new ColorRGBA(1-brightness,0f,brightness,1.0f);
    }
    
}
