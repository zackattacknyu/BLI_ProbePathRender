/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.keyboardTrackingRead;

import com.jme3.input.InputManager;
import org.zrd.utilImpl.mouseKeyboard.GeneralKeyboardActionMethod;

/**
 *
 * @author BLI
 */
public class PositionChange extends GeneralKeyboardActionMethod{
    
    public enum DISP_AXIS {X,Y};
    
    private KeyboardInputPositionChange currentPosChange;
    private float factor = 1;
    private DISP_AXIS axis;

    public PositionChange(InputManager manager, String name, int keyCode, 
            KeyboardInputPositionChange currentPosChange, PositionChange.DISP_AXIS axis, boolean posDirection){
        super(manager,name,keyCode);
        this.currentPosChange = currentPosChange;
        if(!posDirection) factor = -1;
        this.axis = axis;
    }
    
    
    @Override
    public void actionMethod() {
        switch(axis){
            case X:
                currentPosChange.setXDisp(factor);
                break;
                
            case Y:
                currentPosChange.setYDisp(factor);
                break;
        }
    }
    
}
