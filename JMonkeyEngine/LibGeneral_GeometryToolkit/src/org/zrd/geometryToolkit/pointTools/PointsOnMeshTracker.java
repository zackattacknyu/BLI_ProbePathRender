/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.pointTools;

import com.jme3.math.Vector3f;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;

/**
 *
 * @author BLI
 */
public interface PointsOnMeshTracker {
    
    MeshTriangle getCurrentTriangleOnMesh();
    Vector3f getCurrentPointOnMesh();
    
}
