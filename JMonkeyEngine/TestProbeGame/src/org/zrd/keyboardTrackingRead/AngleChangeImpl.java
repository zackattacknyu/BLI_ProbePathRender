/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.keyboardTrackingRead;

import com.jme3.input.InputManager;
import org.zrd.utilImpl.mouseKeyboard.GeneralKeyboardActionMethod;

/**
 *
 * @author Zach
 */
public class AngleChangeImpl extends GeneralKeyboardActionMethod implements AngleChange{
    
    private KeyboardInputAngles currentAngles;
    private AngleChange.ANGLE_TYPE type;
    private float angleDiff;
    
    public AngleChangeImpl(InputManager manager, String name, int keyCode, 
            KeyboardInputAngles currentAngles, AngleChange.ANGLE_TYPE type, boolean increment){
        super(manager,name,keyCode);
        this.currentAngles = currentAngles;
        this.type = type;
        if(increment){
            angleDiff = AngleChange.POS_FACTOR*AngleChange.ANGLE_CHANGE_RADIANS;
        }else{
            angleDiff = AngleChange.NEG_FACTOR*AngleChange.ANGLE_CHANGE_RADIANS;
        }
    }

    @Override
    public void actionMethod() {
        switch(type){
            case YAW:
                changeYaw(angleDiff);
                break;
            case PITCH:
                changePitch(angleDiff);
                break;
            case ROLL: 
                changeRoll(angleDiff);
                break;
        }
    }

    public void changeYaw(float change) {
        currentAngles.changeYaw(change);
    }

    public void changeRoll(float change) {
        currentAngles.changeRoll(change);
    }

    public void changePitch(float change) {
        currentAngles.changePitch(change);
    }
}
