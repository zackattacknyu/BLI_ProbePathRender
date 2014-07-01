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
    private KeyboardInputPositionChange currentPosChange;
    private InputManager inputManager;
    
    public KeyboardTrackingImpl(InputManager inputManager){
        currentAngles = new KeyboardInputAngles();
        currentPosChange = new KeyboardInputPositionChange();
        this.inputManager = inputManager;
        addAngleListeners();
        addPositionChangeListeners();
    }
    
    private void addAngleListeners() {
        
        addAngleListener("pitchLeft",KeyInput.KEY_NUMPAD7,
                AngleChange.ANGLE_TYPE.PITCH,true);
        addAngleListener("pitchRight",KeyInput.KEY_NUMPAD9,
                AngleChange.ANGLE_TYPE.PITCH,false);
        
        addAngleListener("rollForward",KeyInput.KEY_NUMPAD5,
                AngleChange.ANGLE_TYPE.ROLL,true);
        addAngleListener("rollBackward",KeyInput.KEY_NUMPAD0,
                AngleChange.ANGLE_TYPE.ROLL,false);
        
        addAngleListener("rotateClockwise",KeyInput.KEY_NUMPAD1,
                AngleChange.ANGLE_TYPE.YAW,true);
        addAngleListener("rotateCounterclockwise",KeyInput.KEY_NUMPAD3,
                AngleChange.ANGLE_TYPE.YAW,false);
    }

    private void addAngleListener(String name, int keyCode, AngleChange.ANGLE_TYPE type, boolean inc) {
        new AngleChangeImpl(inputManager,name,keyCode,currentAngles, type,inc);
    }

    public KeyboardInputAngles getCurrentAngles() {
        return currentAngles;
    }

    private void addPositionChangeListeners() {
        addPositionChangeListener("moveUp",KeyInput.KEY_NUMPAD8,PositionChangeImpl.DISP_AXIS.Y,true);
        addPositionChangeListener("moveDown",KeyInput.KEY_NUMPAD2,PositionChangeImpl.DISP_AXIS.Y,false);
        
        addPositionChangeListener("moveRight",KeyInput.KEY_NUMPAD6,PositionChangeImpl.DISP_AXIS.X,true);
        addPositionChangeListener("moveLeft",KeyInput.KEY_NUMPAD4,PositionChangeImpl.DISP_AXIS.X,false);
    }
    
    private void addPositionChangeListener(String name, int keyCode, PositionChangeImpl.DISP_AXIS axis, boolean posDir){
        new PositionChangeImpl(inputManager,name,keyCode,currentPosChange,axis,posDir);
    }

    public KeyboardInputPositionChange getCurrentPosChange() {
        return currentPosChange;
    }
    
}
