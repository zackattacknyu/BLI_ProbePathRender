/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.locationTracking;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;

/**
 *
 * @author BLI
 */
public interface LocationTracker {
    
    float getTrackingQuality();
    
    Vector3f getCurrentPosition();
    Vector3f getCurrentDisplacement();
    Vector3f getDisplacementSinceLastPoint();
    void setCurrentTriangle(MeshTriangle triangle);
    void setCurrentPosition(Vector3f position);
    void resetDisplacementSinceLastPoint();
    void updateData();
    
    String getXYZtext();
    String getYawPitchRollText();
    
    boolean isRecordingPath();
    
    Quaternion getLocalRotation();
    
    ArrayList<Vector3f> getVerticesSinceLastRead();
    float getArcLengthSinceLastRead();
    
}
