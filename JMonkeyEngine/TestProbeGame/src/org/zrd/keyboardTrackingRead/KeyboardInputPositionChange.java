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

    
    private float currentXDisp = 0;
    private float currentYDisp = 0;
    
    public void setXDisp(float disp){
        currentXDisp = disp;
    }
    public void setYDisp(float disp){
        currentYDisp = disp;
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
