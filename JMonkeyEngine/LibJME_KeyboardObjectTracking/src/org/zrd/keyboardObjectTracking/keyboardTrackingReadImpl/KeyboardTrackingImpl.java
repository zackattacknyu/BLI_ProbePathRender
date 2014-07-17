/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.keyboardObjectTracking.keyboardTrackingReadImpl;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import org.zrd.keyboardObjectTracking.keyboardTrackingRead.AngleChange;
import org.zrd.keyboardObjectTracking.keyboardTrackingRead.KeyboardTracking;

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
    
    /*
     * The following booleans are used for which angle direction
     *      is considered a positive angle direction
     */
    public static final boolean PITCH_LEFT_IS_POSITIVE = true;
    public static final boolean ROLL_FORWARD_IS_POSITIVE = true;
    public static final boolean ROTATE_CLOCKWISE_IS_POSITIVE = true;
    
    //if true, then left is considered a positive displacement. 
    public static final boolean INVERT_X_AXIS_FOR_DISPLACEMENT = false;
    
    //if true, then up is considered a positive displacement
    public static final boolean INVERT_Y_AXIS_FOR_DISPLACEMENT = false;
    
    /*
     * The following strings are keycodes 
     * for the keyboard tracking actions
     */
    
    public static final int PITCH_LEFT_KEY = KeyInput.KEY_NUMPAD7;
    public static final int PITCH_RIGHT_KEY = KeyInput.KEY_NUMPAD9;
    
    public static final int ROLL_FORWARD_KEY = KeyInput.KEY_NUMPAD5;
    public static final int ROLL_BACKWARD_KEY = KeyInput.KEY_NUMPAD0;
    
    public static final int ROTATE_CLOCKWISE_KEY = KeyInput.KEY_NUMPAD1;
    public static final int ROTATE_COUNTERCLOCKWISE_KEY = KeyInput.KEY_NUMPAD3;
    
    public static final int MOVE_UP_KEY = KeyInput.KEY_NUMPAD8;
    public static final int MOVE_DOWN_KEY = KeyInput.KEY_NUMPAD2;
    
    public static final int MOVE_LEFT_KEY = KeyInput.KEY_NUMPAD4;
    public static final int MOVE_RIGHT_KEY = KeyInput.KEY_NUMPAD6;

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
        addAngleListener(PITCH_LEFT_LISTENER_NAME,PITCH_LEFT_KEY,
                AngleChange.ANGLE_TYPE.PITCH,PITCH_LEFT_IS_POSITIVE);
    }

    @Override
    protected void addPitchRightListener() {
        addAngleListener(PITCH_RIGHT_LISTENER_NAME,PITCH_RIGHT_KEY,
                AngleChange.ANGLE_TYPE.PITCH,!PITCH_LEFT_IS_POSITIVE);
    }

    @Override
    protected void addRollBackwardListener() {
        addAngleListener(ROLL_BACKWARD_LISTENER_NAME,ROLL_BACKWARD_KEY,
                AngleChange.ANGLE_TYPE.ROLL,!ROLL_FORWARD_IS_POSITIVE);
    }

    @Override
    protected void addRollForwardListener() {
        addAngleListener(ROLL_FORWARD_LISTENER_NAME,ROLL_FORWARD_KEY,
                AngleChange.ANGLE_TYPE.ROLL,ROLL_FORWARD_IS_POSITIVE);
    }

    @Override
    protected void addRotateClockwiseListener() {
        addAngleListener(ROTATE_CLOCKWISE_LISTENER_NAME,ROTATE_CLOCKWISE_KEY,
                AngleChange.ANGLE_TYPE.YAW,ROTATE_CLOCKWISE_IS_POSITIVE);
    }

    @Override
    protected void addRotateCounterClockwiseListener() {
        addAngleListener(ROTATE_COUNTERCLOCKWISE_LISTENER_NAME,
                ROTATE_COUNTERCLOCKWISE_KEY,
                AngleChange.ANGLE_TYPE.YAW,!ROTATE_CLOCKWISE_IS_POSITIVE);
    }

    @Override
    protected void addMoveUpListener() {
        addPositionChangeListener(MOVE_UP_LISTENER_NAME,MOVE_UP_KEY,
                PositionChangeImpl.DISP_AXIS.Y,!INVERT_Y_AXIS_FOR_DISPLACEMENT);
    }

    @Override
    protected void addMoveDownListener() {
        addPositionChangeListener(MOVE_DOWN_LISTENER_NAME,MOVE_DOWN_KEY,
                PositionChangeImpl.DISP_AXIS.Y,INVERT_Y_AXIS_FOR_DISPLACEMENT);
    }

    @Override
    protected void addMoveLeftListener() {
        addPositionChangeListener(MOVE_LEFT_LISTENER_NAME,MOVE_LEFT_KEY,
                PositionChangeImpl.DISP_AXIS.X,INVERT_X_AXIS_FOR_DISPLACEMENT);
    }

    @Override
    protected void addMoveRightListener() {
        addPositionChangeListener(MOVE_RIGHT_LISTENER_NAME,MOVE_RIGHT_KEY,
                PositionChangeImpl.DISP_AXIS.X,!INVERT_X_AXIS_FOR_DISPLACEMENT);
    }
    
}
