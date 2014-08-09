/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.pointTools;

import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;

/**
 *
 * @author BLI
 */
public interface MeshPointHandler {
    
    /**
     * This handles a new mesh point. It should be used if the triangle
     *      is the original one on the mesh, not the one after transformation
     * @param pointOnMesh       point on current mesh
     * @param triangleOnMesh    original, untransformed triangle on mesh
     */
    void handleNewMeshPoint(Vector3f pointOnMesh, Triangle triangleOnMesh);
    
    /**
     * This handles a new mesh point. It should be sued if the triangle
     *      is the transformed one in the data structure, not the original one
     * @param pointOnMesh       point on current mesh
     * @param triangleOnMesh    triangle on current mesh
     */
    void handleNewMeshPoint(Vector3f pointOnMesh, MeshTriangle triangleOnMesh);
}
