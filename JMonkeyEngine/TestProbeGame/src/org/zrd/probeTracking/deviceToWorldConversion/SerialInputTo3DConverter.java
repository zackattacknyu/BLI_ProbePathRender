/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking.deviceToWorldConversion;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author BLI
 */
public class SerialInputTo3DConverter extends AbstractSerialInputToWorldConverter{
    

    @Override
    protected Vector3f getRotatedVector(Vector3f inputVector, float yaw, float pitch, float roll) {
        Quaternion rotation = TrackingHelper.getQuarternion(yaw, pitch, roll);
        return rotation.mult(inputVector);
    }
    
}
