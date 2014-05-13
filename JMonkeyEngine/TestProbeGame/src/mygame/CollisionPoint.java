/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.collision.CollisionResult;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author BLI
 */
public class CollisionPoint {
    
    private Vector3f normal;
    private Vector3f contactPoint;
    private Quaternion rotation;

    public CollisionPoint(CollisionResult point) {
        
        normal = point.getContactNormal();
        contactPoint = point.getContactPoint();
        
        calculateRotation();
        
    }
    
    private void calculateRotation(){
        
        Vector3f baselineNormal = new Vector3f(-1,0,0);
        float rotAngle = (float) Math.acos(baselineNormal.dot(normal));
        Vector3f rotAxis = baselineNormal.cross(normal);
        rotation = new Quaternion();
        rotation.fromAngleAxis(rotAngle, rotAxis);
        
    }

    public Vector3f getContactPoint() {
        return contactPoint;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public Vector3f getNormal() {
        return normal;
    }
    
    
    
    
    
    
}
