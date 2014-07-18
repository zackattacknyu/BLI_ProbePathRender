/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.locationTracking;

import com.jme3.math.Vector3f;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;

/**
 *
 * @author BLI
 */
public interface RotationCalibrationTool {
    
    boolean isCalibrationDone();
    boolean isCalibrationNewlyFinished();
    MeshTriangle getTriangleAtEndPoint();
    Vector3f getCalibEndPoint();
    
}
