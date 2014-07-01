/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.keyboardTrackingReadImpl;

import com.jme3.input.InputManager;
import org.zrd.keyboardTrackingRead.KeyboardInputPositionChange;
import org.zrd.keyboardTrackingRead.PositionChange;
import org.zrd.utilImpl.mouseKeyboard.GeneralKeyboardActionMethod;

/**
 *
 * @author BLI
 */
public class PositionChangeImpl extends GeneralKeyboardActionMethod implements PositionChange{
    
    public enum DISP_AXIS {X,Y};
    
    private KeyboardInputPositionChange currentPosChange;
    private float positionDiff;
    private DISP_AXIS axis;

    public PositionChangeImpl(InputManager manager, String name, int keyCode, 
            KeyboardInputPositionChange currentPosChange, PositionChangeImpl.DISP_AXIS axis, boolean posDirection){
        super(manager,name,keyCode);
        this.currentPosChange = currentPosChange;
        if(posDirection){
            positionDiff = POS_FACTOR*POSITION_CHANGE;
        }else{
            positionDiff = NEG_FACTOR*POSITION_CHANGE;
        }
        this.axis = axis;
    }
    
    
    @Override
    public void actionMethod() {
        switch(axis){
            case X:
                changeX(positionDiff);
                break;
                
            case Y:
                changeY(positionDiff);
                break;
        }
    }
    
    public void changeX(float change) {
        currentPosChange.setXDisp(change);
    }

    public void changeY(float change) {
        currentPosChange.setYDisp(change);
    }
    
}
