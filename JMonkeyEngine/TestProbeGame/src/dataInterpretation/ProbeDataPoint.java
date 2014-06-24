/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataInterpretation;

/**
 *
 * @author BLI
 */
public class ProbeDataPoint {

    private float yaw, pitch, roll, x, y;

    public ProbeDataPoint(float yaw, float pitch, float roll, float x, float y) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
        this.x = x;
        this.y = y;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public float getRoll() {
        return roll;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    
    
    
}
