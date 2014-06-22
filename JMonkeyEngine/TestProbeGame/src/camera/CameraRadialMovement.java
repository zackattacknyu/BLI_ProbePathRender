/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package camera;

/**
 *
 * @author Zach
 */
public interface CameraRadialMovement {
    public static final float MOVE_AMOUNT_INWARD = -1.0F / 5.0F;
    public static final float MOVE_AMOUNT_OUTWARD = 1.0F / 5.0F;

    public boolean isInward();
    public void move(float amount);
}
