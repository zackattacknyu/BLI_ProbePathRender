/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.dataHelp;

/**
 *
 * @author BLI
 */
public class DisplacementHelper {
    
    public static float negativeIfTrue(float number, boolean negate){
        return negate ? -1*number : number;
    }
    
    public static float getDisplacement(float lowByte, float highByte){
        
        float total = highByte*256 + lowByte;
        if(total > 32767){
            total = total - 65536;
        }
        
        /*float highByteSign = 1;
        if(highByte > 127){
            highByte = highByte - 256;
            highByteSign = -1;
        }
        
        float highByteMag = Math.abs(highByte);
        
        float totalDisp = highByteSign*(highByteMag*256 + lowByte);
        
        if(Math.abs(totalDisp) > 0){
            System.out.println("HighMag=" + highByteMag + " lowMag=" + lowByte + " total=" + totalDisp);
        }
        
        float totalDisp = highByte*256 + lowByte;
        */
        
        return total;
        
    }
    
}
