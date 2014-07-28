/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.geometryUtil;

import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;

/**
 *
 * @author BLI
 */
public interface MeshPointHandler {
    
    void handleNewMeshPoint(Vector3f pointOnMesh, Triangle triangleOnMesh);
    void handleNewMeshPoint(Vector3f pointOnMesh, MeshTriangle triangleOnMesh);
    
}
