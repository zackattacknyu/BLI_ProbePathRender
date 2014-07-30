/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking.deviceToWorldConversion;

import org.zrd.geometryToolkit.geometricCalculations.RotationHelper;
import com.jme3.math.Quaternion;

/**
 *
 * @author BLI
 */
public class SerialInputTo3DConverter extends AbstractSerialInputToWorldConverter{

    @Override
    protected Quaternion getRotationQuat(float yaw, float pitch, float roll) {
        return RotationHelper.getQuaternion(yaw, pitch, roll);
    }
    
}
