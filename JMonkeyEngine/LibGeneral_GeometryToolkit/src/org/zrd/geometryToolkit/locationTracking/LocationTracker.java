/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.locationTracking;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author BLI
 */
public interface LocationTracker {
    
    Vector3f getCurrentPosition();
    Vector3f getCurrentDisplacement();
    Vector3f getDisplacementSinceLastPoint();
    void resetDisplacementSinceLastPoint();
    void updateData();
    
    String getXYZtext();
    String getYawPitchRollText();
    
    boolean isRecordingPath();
    
    Quaternion getLocalRotation();
    
}
