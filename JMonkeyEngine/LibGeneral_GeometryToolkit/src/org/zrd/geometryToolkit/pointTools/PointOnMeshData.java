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
public class PointOnMeshData extends PointData{
    
    protected MeshTriangle triangleContainingPoint;
    protected Triangle triangleWithPt;
    
    public PointOnMeshData(Vector3f coords, MeshTriangle triangleWithPt){
        super(coords);
        triangleContainingPoint = triangleWithPt;
    }
    
    public PointOnMeshData(Vector3f coords, Triangle triangleWithPt){
        super(coords);
        this.triangleWithPt = triangleWithPt;
    }
    
    public PointOnMeshData(Vector3f coords, MeshTriangle triangle, Triangle triangleWithPt){
        super(coords);
        this.triangleContainingPoint = triangle;
        this.triangleWithPt = triangleWithPt;
    }

    public MeshTriangle getTriangleContainingPoint() {
        return triangleContainingPoint;
    }

    public Triangle getTriangleWithPt() {
        return triangleWithPt;
    }

    
    
    
    
}
