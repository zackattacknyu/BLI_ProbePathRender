/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking.deviceToWorldConversion;

import com.jme3.math.Quaternion;

/**
 *
 * @author BLI
 */
public class SerialInputTo2DConverter extends AbstractSerialInputToWorldConverter{

    @Override
    protected Quaternion getRotationQuat(float yaw, float pitch, float roll) {
        return new Quaternion();
    }
    
}
