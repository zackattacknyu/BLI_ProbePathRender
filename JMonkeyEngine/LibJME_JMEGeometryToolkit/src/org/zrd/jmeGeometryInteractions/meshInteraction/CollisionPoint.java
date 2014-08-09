/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryInteractions.meshInteraction;

import com.jme3.collision.CollisionResult;
import org.zrd.geometryToolkit.pointTools.PointOnMeshData;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;

/**
 * This is a data struture that calculates and stores
 *      the data from when the mouse is clicked and it hits
 *      the mesh
 *
 * @author BLI
 */
public class CollisionPoint extends PointOnMeshData{
    
    /**
     * This makes the collisionPoint object
     * @param point     CollisionResult object from the application
     */
    public CollisionPoint(CollisionResult point) {

        super(point.getContactPoint(),point.getTriangle(null));
        
    }
    
    /*
     * IMPORTANT NOTE: 
     *  THIS IS OLD CODE THAT IS NO LONGER USED. 
     *  IT SHOULD BE KEPT IF WE DECIDE TO CALCULATE
     *      ROTATION AT AN INTERSECTION POINT
     * 
     *
    private void calculateRotation(){
       
        float rotAngle = (float) Math.acos(BASELINE_NORMAL.dot(normal));
        Vector3f rotAxis = BASELINE_NORMAL.cross(normal);
        rotation = new Quaternion();
        rotation.fromAngleAxis(rotAngle, rotAxis);
        
    }*/

    
    
    
    
    
    
    
}
