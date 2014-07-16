/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometry.renderedObjects;

import com.jme3.scene.Spatial;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;

/**
 *
 * @author Zach
 */
public class RenderedMesh {
    
    protected Spatial surfaceMesh;
    protected TriangleSet activeMeshInfo;

    public Spatial getSurfaceMesh() {
        return surfaceMesh;
    }

    public TriangleSet getActiveMeshInfo() {
        return activeMeshInfo;
    }
    
}
