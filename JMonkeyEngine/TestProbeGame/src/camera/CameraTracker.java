/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package camera;

import cameraImpl.CameraTrackerImpl;
import com.jme3.math.Vector3f;

/**
 *
 * @author BLI
 */
public abstract class CameraTracker {
    protected static Vector3f LEFT_RIGHT_AXIS = Vector3f.UNIT_Y;
    protected static final float ROTATION_AMOUNT = 1.0F / 20.0F;
    protected static final float ROTATION_AMOUNT_NEG = -1 * CameraTrackerImpl.ROTATION_AMOUNT;
    protected static Vector3f UP_DOWN_AXIS = Vector3f.UNIT_X;
    
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
