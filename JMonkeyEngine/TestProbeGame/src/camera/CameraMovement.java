/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package camera;

/** This is used to rotate the camera viewpoint itself
 *      up, down, left, or right
 *
 * @author Zach
 */
public interface CameraMovement {
    public void moveCameraUp();
    public void moveCameraDown();
    public void moveCameraLeft();
    public void moveCameraRight();
    public void moveCamera();
}
