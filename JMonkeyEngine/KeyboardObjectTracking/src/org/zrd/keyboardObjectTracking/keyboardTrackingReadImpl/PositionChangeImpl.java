/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.keyboardObjectTracking.keyboardTrackingReadImpl;

import com.jme3.input.InputManager;
import org.zrd.keyboardObjectTracking.keyboardTrackingRead.KeyboardInputPositionChange;
import org.zrd.keyboardObjectTracking.keyboardTrackingRead.PositionChange;
import org.zrd.renderUtil.mouseKeyboard.GeneralKeyboardActionMethod;

/**This implements position changes via the keyboard input
 *
 * @author BLI
 */
public class PositionChangeImpl extends GeneralKeyboardActionMethod implements PositionChange{
    
    /**
     * Whether or not the change is in the x or y axis
     */
    public enum DISP_AXIS {X,Y};
    
    private KeyboardInputPositionChange currentPosChange;
    private float positionDiff;
    private DISP_AXIS axis;

    /**
     * Constructs the position change listener
     * @param manager               the inputManager that keeps track of keyboard events
     * @param name                  name of listener
     * @param keyCode               keycode of the keyboard key that will be used to change position
     * @param currentPosChange      current position change object
     * @param axis                  whether it's the x or y axis that the displacement occurs on
     * @param posDirection          whether the displacement is in the positive or negative direction
     */
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
