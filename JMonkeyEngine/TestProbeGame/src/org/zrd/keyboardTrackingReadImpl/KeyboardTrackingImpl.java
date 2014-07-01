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
 *This implements all the object tracking using 
 *      just keyboard inputs. It adds the listeners
 *      which manipulate the values of yaw, pitch, roll
 *      as well as x,y displacement from the keyboard.
 * 
 * 
 * @author Zach
 */
public class KeyboardTrackingImpl extends KeyboardTracking{
    
    

    private InputManager inputManager;
    
    /**
     * Constructs the keyboard tracker
     * @param inputManager      inputManger that keeps track of keyboard events
     */
    public KeyboardTrackingImpl(InputManager inputManager){
        super();
        this.inputManager = inputManager;
        addListeners();
    }

    /**
     * This adds a listener for an angle change
     * @param name      name of listener
     * @param keyCode   keycode for angle change
     * @param type      which angle to change (yaw, pitch, or roll)
     * @param inc       whether or not to increment or decremetn
     */
    private void addAngleListener(String name, int keyCode, AngleChange.ANGLE_TYPE type, boolean inc) {
        new AngleChangeImpl(inputManager,name,keyCode,currentAngles, type,inc);
    }

    /**
     * This adds a listener for a position change
     * @param name      name of listener
     * @param keyCode   keycode for position change
     * @param axis      whether it's x or y axis that will change
     * @param posDir    whether or not the displacement is in the positive or negative direction
     */
    private void addPositionChangeListener(String name, int keyCode, PositionChangeImpl.DISP_AXIS axis, boolean posDir){
        new PositionChangeImpl(inputManager,name,keyCode,currentPosChange,axis,posDir);
    }

    @Override
    protected void addPitchLeftListener() {
        addAngleListener(PITCH_LEFT_LISTENER_NAME,KeyInput.KEY_NUMPAD7,
                AngleChange.ANGLE_TYPE.PITCH,true);
    }

    @Override
    protected void addPitchRightListener() {
        addAngleListener(PITCH_RIGHT_LISTENER_NAME,KeyInput.KEY_NUMPAD9,
                AngleChange.ANGLE_TYPE.PITCH,false);
    }

    @Override
    protected void addRollBackwardListener() {
        addAngleListener(ROLL_BACKWARD_LISTENER_NAME,KeyInput.KEY_NUMPAD0,
                AngleChange.ANGLE_TYPE.ROLL,false);
    }

    @Override
    protected void addRollForwardListener() {
        addAngleListener(ROLL_FORWARD_LISTENER_NAME,KeyInput.KEY_NUMPAD5,
                AngleChange.ANGLE_TYPE.ROLL,true);
    }

    @Override
    protected void addRotateClockwiseListener() {
        addAngleListener(ROTATE_CLOCKWISE_LISTENER_NAME,KeyInput.KEY_NUMPAD1,
                AngleChange.ANGLE_TYPE.YAW,true);
    }

    @Override
    protected void addRotateCounterClockwiseListener() {
        addAngleListener(ROTATE_COUNTERCLOCKWISE_LISTENER_NAME,KeyInput.KEY_NUMPAD3,
                AngleChange.ANGLE_TYPE.YAW,false);
    }

    @Override
    protected void addMoveUpListener() {
        addPositionChangeListener(MOVE_UP_LISTENER_NAME,KeyInput.KEY_NUMPAD8,
                PositionChangeImpl.DISP_AXIS.Y,true);
    }

    @Override
    protected void addMoveDownListener() {
        addPositionChangeListener(MOVE_DOWN_LISTENER_NAME,KeyInput.KEY_NUMPAD2,
                PositionChangeImpl.DISP_AXIS.Y,false);
    }

    @Override
    protected void addMoveLeftListener() {
        addPositionChangeListener(MOVE_LEFT_LISTENER_NAME,KeyInput.KEY_NUMPAD4,
                PositionChangeImpl.DISP_AXIS.X,false);
    }

    @Override
    protected void addMoveRightListener() {
        addPositionChangeListener(MOVE_RIGHT_LISTENER_NAME,KeyInput.KEY_NUMPAD6,
                PositionChangeImpl.DISP_AXIS.X,true);
    }
    
}
