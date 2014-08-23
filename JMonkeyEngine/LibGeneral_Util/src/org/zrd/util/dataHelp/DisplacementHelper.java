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
    
    public static float getDisplacement(float lowByte, float highByte){
        
        if(highByte > 127){
            highByte = highByte - 256;
        }
        
        return highByte*256+lowByte;
        
    }
    
}
