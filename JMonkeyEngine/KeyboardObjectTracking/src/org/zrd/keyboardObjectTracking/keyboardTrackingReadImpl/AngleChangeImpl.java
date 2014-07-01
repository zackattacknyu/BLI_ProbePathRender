/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.keyboardObjectTracking.keyboardTrackingReadImpl;

import com.jme3.input.InputManager;
import org.zrd.keyboardObjectTracking.keyboardTrackingRead.AngleChange;
import org.zrd.keyboardObjectTracking.keyboardTrackingRead.KeyboardInputAngles;
import org.zrd.renderUtil.mouseKeyboard.GeneralKeyboardActionMethod;

/**This implements an individual angle change action from the keyboard
 *
 * @author Zach
 */
public class AngleChangeImpl extends GeneralKeyboardActionMethod implements AngleChange{
    
    private KeyboardInputAngles currentAngles;
    private AngleChange.ANGLE_TYPE type;
    private float angleDiff;
    
    /**
     * Instantiates an angle change
     * @param manager           inputManager object that keeps track of keyboard events
     * @param name              name of action
     * @param keyCode           keycode on keyboard for this action
     * @param currentAngles     object for keeping track of current angles from keyboard input
     * @param type              type of angle (yaw, pitch, or roll) that we will change
     * @param increment         whether we will increment or decrement the angle with this action
     */
    public AngleChangeImpl(InputManager manager, String name, int keyCode, 
            KeyboardInputAngles currentAngles, AngleChange.ANGLE_TYPE type, boolean increment){
        super(manager,name,keyCode);
        this.currentAngles = currentAngles;
        this.type = type;
        if(increment){
            angleDiff = POS_FACTOR*ANGLE_CHANGE_RADIANS;
        }else{
            angleDiff = NEG_FACTOR*ANGLE_CHANGE_RADIANS;
        }
    }

    /**
     * This is the method that gets called if the keystroke is pressed.
     * It calls the methods that increment or decrement the
     *      yaw, pitch, or roll values
     */
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
