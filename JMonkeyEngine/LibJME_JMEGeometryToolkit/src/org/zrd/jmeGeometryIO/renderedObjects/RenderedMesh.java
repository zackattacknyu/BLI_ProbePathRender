/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryIO.renderedObjects;

import com.jme3.scene.Spatial;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;

/**
 * 
 * This stored a pre-selected mesh
 *      and its triangle data
 *
 * @author Zach
 */
public class RenderedMesh {
    
    protected Spatial surfaceMesh;
    protected TriangleSet activeMeshInfo;

    /**
     * Get the rendered mesh
     * @return  jme spatial for the mesh
     */
    public Spatial getSurfaceMesh() {
        return surfaceMesh;
    }

    /**
     * Gets the triangle set data for the mesh
     * @return      triangleSet for mesh
     */
    public TriangleSet getActiveMeshInfo() {
        return activeMeshInfo;
    }
    
}
