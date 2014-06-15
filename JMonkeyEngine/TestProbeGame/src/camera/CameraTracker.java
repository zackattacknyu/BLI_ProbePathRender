/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package camera;

/**
 *
 * @author BLI
 */
public abstract class CameraTracker {
    
    protected void addListeners(){
        addMovementListeners();
        addRadialMovementListeners();
        addRotationListeners();
    }
    
    protected void enableDefaults(){
        setDefaultCamera();
        enableFlyCam();
    }
    
    private void addRotationListeners(){
        rotateUp();
        rotateDown();
        rotateLeft();
        rotateRight();
    }
    
    protected abstract void rotateUp();
    protected abstract void rotateDown();
    protected abstract void rotateLeft();
    protected abstract void rotateRight();
    
    protected abstract void moveUp();
    protected abstract void moveDown();
    protected abstract void moveLeft();
    protected abstract void moveRight();
    
    private void addMovementListeners(){
        moveUp(); 
        moveDown();
        moveLeft();
        moveRight();
    }
    
    private void addRadialMovementListeners(){
        moveInward();
        moveOutward();
    }

    protected abstract void moveInward();
    protected abstract void moveOutward();
    
    protected abstract void setDefaultCamera();
    public abstract void setDefaultCamera(boolean sphereOn);
    protected abstract void enableFlyCam();
    
}
