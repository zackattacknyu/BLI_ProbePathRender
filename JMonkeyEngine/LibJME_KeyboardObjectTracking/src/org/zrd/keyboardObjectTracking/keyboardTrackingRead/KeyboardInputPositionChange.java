/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.keyboardObjectTracking.keyboardTrackingRead;

/**This simply tracks the current x and y displacement
 *      that is coming from keyboard input
 * 
 * The listener will set the current displacement.
 *      Then, once the application requests the displacemet,
 *          it will be sent and then reset to zero.
 *
 * @author BLI
 */
public class KeyboardInputPositionChange {
    
    private float currentXDisp = 0;
    private float currentYDisp = 0;
    
    /**
     * This sets the current x displacement
     * @param disp      value of current x displacement
     */
    public void setXDisp(float disp){
        currentXDisp = disp;
    }
    
    /**
     * This sets the current y displacement
     * @param disp      value of current y displacement
     */
    public void setYDisp(float disp){
        currentYDisp = disp;
    }
    
    /**
     * This gets the current x displacment and then zeros the 
     *      x displacement
     * @return      current x displacement
     */
    public float getXDisp(){
        float returnX = currentXDisp;
        currentXDisp = 0;
        return returnX;
    }
    
    /**
     * This gets the current y displacement and then zeros the
     *      y displacement
     * @return      current y displacement
     */
    public float getYDisp(){
        float returnY = currentYDisp;
        currentYDisp = 0;
        return returnY;
    }
    
}
