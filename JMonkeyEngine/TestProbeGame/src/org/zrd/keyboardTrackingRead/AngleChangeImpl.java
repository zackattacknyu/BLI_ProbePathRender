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
    private float factor = 1;
    
    public AngleChangeImpl(InputManager manager, String name, int keyCode, 
            KeyboardInputAngles currentAngles, AngleChange.ANGLE_TYPE type, boolean increment){
        super(manager,name,keyCode);
        this.currentAngles = currentAngles;
        this.type = type;
        if(!increment) factor = -1;
    }

    @Override
    public void actionMethod() {
        switch(type){
            case YAW:
                changeYaw(factor);
                break;
            case PITCH:
                changePitch(factor);
                break;
            case ROLL: 
                changeRoll(factor);
                break;
        }
    }

    public void changeYaw(float factor) {
        currentAngles.changeYaw(factor);
    }

    public void changeRoll(float factor) {
        currentAngles.changeRoll(factor);
    }

    public void changePitch(float factor) {
        currentAngles.changePitch(factor);
    }
}
