/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.keyboardTrackingRead;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;

/**
 *
 * @author Zach
 */
public class KeyboardTrackingImpl {

    private KeyboardInputAngles currentAngles;
    private InputManager inputManager;
    
    public KeyboardTrackingImpl(InputManager inputManager){
        currentAngles = new KeyboardInputAngles();
        this.inputManager = inputManager;
        addListeners();
    }
    
    private void addListeners() {
        
        addListener("pitchLeft",KeyInput.KEY_NUMPAD7,
                AngleChange.ANGLE_TYPE.PITCH,true);
        addListener("pitchRight",KeyInput.KEY_NUMPAD9,
                AngleChange.ANGLE_TYPE.PITCH,false);
        
        addListener("rollForward",KeyInput.KEY_NUMPAD5,
                AngleChange.ANGLE_TYPE.ROLL,true);
        addListener("rollBackward",KeyInput.KEY_NUMPAD0,
                AngleChange.ANGLE_TYPE.ROLL,false);
        
        addListener("rotateClockwise",KeyInput.KEY_NUMPAD1,
                AngleChange.ANGLE_TYPE.YAW,true);
        addListener("rotateCounterclockwise",KeyInput.KEY_NUMPAD3,
                AngleChange.ANGLE_TYPE.YAW,false);
    }

    private void addListener(String name, int keyCode, AngleChange.ANGLE_TYPE type, boolean inc) {
        new AngleChange(inputManager,name,keyCode,currentAngles, type,inc);
    }

    public KeyboardInputAngles getCurrentAngles() {
        return currentAngles;
    }
    
}
