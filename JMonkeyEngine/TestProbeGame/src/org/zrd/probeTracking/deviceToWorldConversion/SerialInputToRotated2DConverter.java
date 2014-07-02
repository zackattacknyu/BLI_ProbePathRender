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
public class SerialInputToRotated2DConverter extends AbstractSerialInputToWorldConverter{


    @Override
    public Matrix3f getRotationMatrix(float yaw,float pitch, float roll){
        return TrackingHelper.getRotationMatrix(yaw);
    }

    
    
    
}
