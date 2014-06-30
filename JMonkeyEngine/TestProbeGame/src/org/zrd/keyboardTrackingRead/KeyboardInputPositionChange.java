/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.keyboardTrackingRead;

/**
 *
 * @author BLI
 */
public class KeyboardInputPositionChange {
    
    
    public static final float POSITION_CHANGE = 4.0f;
    
    private float currentXDisp = 0;
    private float currentYDisp = 0;
    
    public void setXDisp(float factor){
        currentXDisp = getDisp(factor);
    }
    public void setYDisp(float factor){
        currentYDisp = getDisp(factor);
    }
    private float getDisp(float factor){
        return factor*POSITION_CHANGE;
    }
    
    public float getXDisp(){
        float returnX = currentXDisp;
        currentXDisp = 0;
        return returnX;
    }
    public float getYDisp(){
        float returnY = currentYDisp;
        currentYDisp = 0;
        return returnY;
    }
    
}
