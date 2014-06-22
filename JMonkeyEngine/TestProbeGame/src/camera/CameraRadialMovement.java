/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package camera;

/**This is an interface that is meant
 *      to be used for implementing 
 *      camera radial movement
 *
 * @author Zach
 */
public interface CameraRadialMovement {
    
    /**
     * Amount to move inward or outward in radial direction
     */
    public static final float MOVE_AMOUNT_INWARD = -1.0F / 5.0F;
    public static final float MOVE_AMOUNT_OUTWARD = 1.0F / 5.0F;

    /**
     * tells if the current movement is inward or outward
     * @return      
     */
    public boolean isInward();
    
    /**
     * This is meant to be used to move the camera in the amount given.
     *      The amount should have a different number depending on whether
     *      it is positve or negative.
     * @param amount        amount inward or outward to move camera radially
     */
    public void move(float amount);
}
