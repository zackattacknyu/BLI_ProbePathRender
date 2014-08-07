/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryIO.meshIO;

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
    
    public RenderedMesh(){
        
    }
    public RenderedMesh(Spatial mesh, TriangleSet meshInfo){
        surfaceMesh = mesh;
        activeMeshInfo = meshInfo;
    }

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
