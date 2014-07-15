/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTrackingOnSurface;

import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;

/**
 *
 * @author BLI
 */
public interface MeshPointHandler {
    
    void handleNewMeshPoint(Vector3f pointOnMesh, Triangle triangleOnMesh);
    
}
