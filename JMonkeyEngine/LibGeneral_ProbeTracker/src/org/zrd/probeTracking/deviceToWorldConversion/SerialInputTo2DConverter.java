/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking.deviceToWorldConversion;

import com.jme3.math.Matrix3f;

/**
 *
 * @author BLI
 */
public class SerialInputTo2DConverter extends AbstractSerialInputToWorldConverter{

    @Override
    protected Matrix3f getRotationMatrix(float yaw, float pitch, float roll) {
        return Matrix3f.IDENTITY;
    }
    
}
