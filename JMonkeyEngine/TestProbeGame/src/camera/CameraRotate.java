/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package camera;

import com.jme3.math.Vector3f;

/**
 *
 * @author Zach
 */
public interface CameraRotate {
    public static final Vector3f UP_DOWN_AXIS = Vector3f.UNIT_X;
    public static final Vector3f LEFT_RIGHT_AXIS = Vector3f.UNIT_Y;
    public static final float ROTATION_AMOUNT_NEG = -1.0F / 20.0F;
    public static final float ROTATION_AMOUNT = 1.0F / 20.0F;
    
    public boolean shouldRotate();
    public void changeLocation();
    public void changeLookAt();
    
}
