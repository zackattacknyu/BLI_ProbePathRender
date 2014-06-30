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
public class AngleChangeImpl extends GeneralKeyboardActionMethod{
    
    public enum ANGLE_TYPE{ YAW, PITCH, ROLL};
    
    private KeyboardInputAngles currentAngles;
    private ANGLE_TYPE type;
    private float factor = 1;
    
    public AngleChangeImpl(InputManager manager, String name, int keyCode, 
            KeyboardInputAngles currentAngles, ANGLE_TYPE type, boolean increment){
        super(manager,name,keyCode);
        this.currentAngles = currentAngles;
        this.type = type;
        if(!increment) factor = -1;
    }

    @Override
    public void actionMethod() {
        switch(type){
            case YAW:
                currentAngles.changeYaw(factor);
                break;
            case PITCH:
                currentAngles.changePitch(factor);
                break;
            case ROLL: 
                currentAngles.changeRoll(factor);
                break;
        }
    }
}
