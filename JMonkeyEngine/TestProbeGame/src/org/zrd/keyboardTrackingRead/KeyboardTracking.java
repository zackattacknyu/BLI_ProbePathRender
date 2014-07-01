/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.keyboardTrackingRead;

/**
 *
 * @author BLI
 */
public abstract class KeyboardTracking {
    
    
    protected KeyboardInputAngles currentAngles;
    protected KeyboardInputPositionChange currentPosChange;
    
    public KeyboardTracking(){
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
    
    public KeyboardInputAngles getCurrentAngles() {
        return currentAngles;
    }
    
    public KeyboardInputPositionChange getCurrentPosChange() {
        return currentPosChange;
    }
    
}
