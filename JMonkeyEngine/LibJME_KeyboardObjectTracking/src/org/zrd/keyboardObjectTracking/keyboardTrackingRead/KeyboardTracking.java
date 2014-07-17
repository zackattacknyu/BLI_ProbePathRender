/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.keyboardObjectTracking.keyboardTrackingRead;

/**This is the abstract class for moving around an object
 *  using keyboard inputs.
 * 
 * In this case, we are moving a probe using yaw,pitch,roll angle
 *      and x,y displacement
 *
 * @author BLI
 */
public abstract class KeyboardTracking {
    
    /**
     * The following block of 10 strings is all the listener names.
     * Uniqueness is important here
     * NOTE: Only change these strings if absolutely necessary
     */
    
    //these are listener names for all the angle movements
    public static final String PITCH_LEFT_LISTENER_NAME = "pitchLeft";
    public static final String PITCH_RIGHT_LISTENER_NAME = "pitchRight";
    public static final String ROLL_FORWARD_LISTENER_NAME = "rollForward";
    public static final String ROLL_BACKWARD_LISTENER_NAME = "rollBackward";
    public static final String ROTATE_CLOCKWISE_LISTENER_NAME = "rotateClockwise";
    public static final String ROTATE_COUNTERCLOCKWISE_LISTENER_NAME = "rotateCounterClockwise";
    
    //these are the listener names for the displacements
    public static final String MOVE_UP_LISTENER_NAME = "moveUp";
    public static final String MOVE_DOWN_LISTENER_NAME = "moveDown";
    public static final String MOVE_LEFT_LISTENER_NAME = "moveLeft";
    public static final String MOVE_RIGHT_LISTENER_NAME = "moveRight";
    
    
    protected KeyboardInputAngles currentAngles;
    protected KeyboardInputPositionChange currentPosChange;
    
    
    protected KeyboardTracking(){
        currentAngles = new KeyboardInputAngles();
        currentPosChange = new KeyboardInputPositionChange();
    }
    
    protected void addListeners(){
        addAngleListeners();
        addPositionChangeListeners();
    }

    private void addAngleListeners() {
        addPitchLeftListener();
        addPitchRightListener();

        addRollBackwardListener();
        addRollForwardListener();

        addRotateClockwiseListener();
        addRotateCounterClockwiseListener();
    }
    
    protected abstract void addPitchLeftListener();
    protected abstract void addPitchRightListener();
    
    protected abstract void addRollBackwardListener();
    protected abstract void addRollForwardListener();
    
    protected abstract void addRotateClockwiseListener();
    protected abstract void addRotateCounterClockwiseListener();

    private void addPositionChangeListeners() {
        addMoveUpListener();
        addMoveDownListener();
        
        addMoveLeftListener();
        addMoveRightListener();
    }
    
    protected abstract void addMoveUpListener();
    protected abstract void addMoveDownListener();
    
    protected abstract void addMoveLeftListener();
    protected abstract void addMoveRightListener();
    
    /**
     * Gets the object representing the current angles
     *      that come from keyboard input
     * @return      current angles from keyboard input
     */
    public KeyboardInputAngles getCurrentAngles() {
        return currentAngles;
    }
    
    /**
     * Gets the object representing the current x,y
     *      displacement that comes from the keyboard input
     * @return      current position change from keyboard input
     */
    public KeyboardInputPositionChange getCurrentPosChange() {
        return currentPosChange;
    }
    
}
