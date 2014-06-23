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
        addRotateViewpointListeners();
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
     * This rotates the camera viewpoint up
     */
    protected abstract void rotateViewpointUp();
    
    /**
     * This rotates the camera viewpoint down
     */
    protected abstract void rotateViewpointDown();
    
    /**
     * This rotates the camera viewpoint left
     */
    protected abstract void rotateViewpointLeft();
    
    /**
     * This rotates the camera viewpoint right
     */
    protected abstract void rotateViewpointRight();
    
    /**
     * This adds the rotate movement listeners
     */
    private void addRotateViewpointListeners(){
        rotateViewpointUp(); 
        rotateViewpointDown();
        rotateViewpointLeft();
        rotateViewpointRight();
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
     *      for the most common mode
     */
    protected abstract void setDefaultCamera();
    
    /**
     * This sets the default camera depending on the mode
     * @param mode      mode depending on context
     */
    public abstract void setDefaultCamera(short mode);
    
    /**
     * This is meant to enable the fly by camera (w,a,s,d,q,z buttons)
     */
    protected abstract void enableFlyCam();
    
}
