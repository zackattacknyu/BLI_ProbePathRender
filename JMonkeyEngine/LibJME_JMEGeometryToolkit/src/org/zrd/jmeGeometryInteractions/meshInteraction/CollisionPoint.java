/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryInteractions.meshInteraction;

import com.jme3.collision.CollisionResult;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;

/**
 * This is a data struture that calculates and stores
 *      the data from when the mouse is clicked and it hits
 *      the mesh
 *
 * @author BLI
 */
public class CollisionPoint {

    private Vector3f contactPoint;
    private Triangle triangle;
    
    /**
     * This makes the collisionPoint object
     * @param point     CollisionResult object from the application
     */
    public CollisionPoint(CollisionResult point) {

        contactPoint = point.getContactPoint();
        triangle = point.getTriangle(triangle);
        
    }

    /**
     * Gets the triangle at the collision point
     * @return      jme triangle object for the one hit
     */
    public Triangle getTriangle(){
        return triangle;
    }
    
    /**
     * gets the exact point the ray hit on the mesh
     * @return      point on mesh that the ray hit
     */
    public Vector3f getContactPoint() {
        return contactPoint;
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
