/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.keyboardTrackingReadImpl;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import org.zrd.keyboardTrackingRead.AngleChange;
import org.zrd.keyboardTrackingRead.KeyboardTracking;

/**
 *
 * @author Zach
 */
public class KeyboardTrackingImpl extends KeyboardTracking{

    private InputManager inputManager;
    
    public KeyboardTrackingImpl(InputManager inputManager){
        super();
        this.inputManager = inputManager;
        addListeners();
    }

    private void addAngleListener(String name, int keyCode, AngleChange.ANGLE_TYPE type, boolean inc) {
        new AngleChangeImpl(inputManager,name,keyCode,currentAngles, type,inc);
    }

    private void addPositionChangeListener(String name, int keyCode, PositionChangeImpl.DISP_AXIS axis, boolean posDir){
        new PositionChangeImpl(inputManager,name,keyCode,currentPosChange,axis,posDir);
    }
    
    @Override
    protected void addPitchLeftListener() {
        addAngleListener("pitchLeft",KeyInput.KEY_NUMPAD7,
                AngleChange.ANGLE_TYPE.PITCH,true);
    }

    @Override
    protected void addPitchRightListener() {
        addAngleListener("pitchRight",KeyInput.KEY_NUMPAD9,
                AngleChange.ANGLE_TYPE.PITCH,false);
    }

    @Override
    protected void addRollBackwardListener() {
        addAngleListener("rollBackward",KeyInput.KEY_NUMPAD0,
                AngleChange.ANGLE_TYPE.ROLL,false);
    }

    @Override
    protected void addRollForwardListener() {
        addAngleListener("rollForward",KeyInput.KEY_NUMPAD5,
                AngleChange.ANGLE_TYPE.ROLL,true);
    }

    @Override
    protected void addRotateClockwiseListener() {
        addAngleListener("rotateClockwise",KeyInput.KEY_NUMPAD1,
                AngleChange.ANGLE_TYPE.YAW,true);
    }

    @Override
    protected void addRotateCounterClockwiseListener() {
        addAngleListener("rotateCounterclockwise",KeyInput.KEY_NUMPAD3,
                AngleChange.ANGLE_TYPE.YAW,false);
    }

    @Override
    protected void addMoveUpListener() {
        addPositionChangeListener("moveUp",KeyInput.KEY_NUMPAD8,
                PositionChangeImpl.DISP_AXIS.Y,true);
    }

    @Override
    protected void addMoveDownListener() {
        addPositionChangeListener("moveDown",KeyInput.KEY_NUMPAD2,
                PositionChangeImpl.DISP_AXIS.Y,false);
    }

    @Override
    protected void addMoveLeftListener() {
        addPositionChangeListener("moveLeft",KeyInput.KEY_NUMPAD4,
                PositionChangeImpl.DISP_AXIS.X,false);
    }

    @Override
    protected void addMoveRightListener() {
        addPositionChangeListener("moveRight",KeyInput.KEY_NUMPAD6,
                PositionChangeImpl.DISP_AXIS.X,true);
    }
    
}
