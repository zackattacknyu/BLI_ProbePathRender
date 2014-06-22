/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package camera;

/**This is an abstract class for planned camera tracker 
 *      methods. It includes adding the mouse/keyboard 
 *      listeners for the different movements and enabling
 *      the default cameras
 *
 * @author BLI
 */
public abstract class CameraTracker {
    
    /**
     * This adds all the listeners
     */
    protected void addListeners(){
        addMovementListeners();
        addRadialMovementListeners();
        addRotationListeners();
    }
    
    /**
     * This is used to enable the default settings
     */
    protected void enableDefaults(){
        setDefaultCamera();
        enableFlyCam();
    }
    
    /**
     * This adds the rotation listeners
     */
    private void addRotationListeners(){
        rotateUp();
        rotateDown();
        rotateLeft();
        rotateRight();
    }
    
    /**
     * This rotates the camera up
     */
    protected abstract void rotateUp();
    
    /**
     * This rotates the camera down
     */
    protected abstract void rotateDown();
    
    /**
     * This rotates the camera left
     */
    protected abstract void rotateLeft();
    
    /**
     * This rotates the camera right
     */
    protected abstract void rotateRight();
    
    /**
     * This moves the camera up
     */
    protected abstract void moveUp();
    
    /**
     * This moves the camera down
     */
    protected abstract void moveDown();
    
    /**
     * This moves the camera left
     */
    protected abstract void moveLeft();
    
    /**
     * This moves the camera right
     */
    protected abstract void moveRight();
    
    /**
     * This adds the movement listeners
     */
    private void addMovementListeners(){
        moveUp(); 
        moveDown();
        moveLeft();
        moveRight();
    }
    
    
    /**
     * This addds the radial movement listeners
     */
    private void addRadialMovementListeners(){
        moveInward();
        moveOutward();
    }

    /**
     * This moves the camera inward radially
     */
    protected abstract void moveInward();
    
    /**
     * This moves the camera outward radially
     */
    protected abstract void moveOutward();
    
    /**
     * This sets the default settings for the camera
     */
    protected abstract void setDefaultCamera();
    
    //TODO: Change this to accept a short number which will be used
    //      to denote the mode
    public abstract void setDefaultCamera(boolean sphereOn);
    
    /**
     * This is meant to enable the fly by camera (w,a,s,d,q,z buttons)
     */
    protected abstract void enableFlyCam();
    
}
