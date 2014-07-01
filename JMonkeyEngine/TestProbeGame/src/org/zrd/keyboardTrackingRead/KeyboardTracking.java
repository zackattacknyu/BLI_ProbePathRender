/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.keyboardTrackingRead;

/**This is the abstract class for moving around an object
 *  using keyboard inputs.
 * 
 * In this case, we are moving a probe using yaw,pitch,roll angle
 *      and x,y displacement
 *
 * @author BLI
 */
public abstract class KeyboardTracking {
    
    
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
