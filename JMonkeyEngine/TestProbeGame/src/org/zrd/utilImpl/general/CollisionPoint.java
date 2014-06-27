/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.utilImpl.general;

import com.jme3.collision.CollisionResult;
import com.jme3.math.Quaternion;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import org.zrd.graphicsTools.geometry.meshTraversal.MeshHelper;
import org.zrd.probeTracking.TrackingHelper;

/**
 *
 * @author BLI
 */
public class CollisionPoint {
    
    public static final Vector3f BASELINE_NORMAL = new Vector3f(0,0,-1);
    
    private Vector3f normal;
    private Vector3f contactPoint;
    private Quaternion rotation;
    private Triangle triangle;
    
    private Vector3f vertex1,vertex2,vertex3;
    
    public CollisionPoint(CollisionResult point) {
        
        normal = point.getContactNormal();
        contactPoint = point.getContactPoint();
        triangle = point.getTriangle(triangle);
        vertex1 = triangle.get1(); 
        vertex2 = triangle.get2();
        vertex3 = triangle.get3();
        calculateRotation();
        
    }

    public Vector3f getVertex1() {
        return vertex1;
    }

    public Vector3f getVertex2() {
        return vertex2;
    }

    public Vector3f getVertex3() {
        return vertex3;
    }

    
    
    public Triangle getTriangle(){
        return triangle;
    }
    
    public String getTriangleInfo(){
        return MeshHelper.getTriangleInfo(triangle);
    }
    
    private void calculateRotation(){
       
        float rotAngle = (float) Math.acos(BASELINE_NORMAL.dot(normal));
        Vector3f rotAxis = BASELINE_NORMAL.cross(normal);
        rotation = new Quaternion();
        rotation.fromAngleAxis(rotAngle, rotAxis);
        
        /*float yawAngle = (float)Math.acos(Vector3f.UNIT_Z.dot(normal));
        float rollAngle = (float)Math.acos(Vector3f.UNIT_Y.dot(normal));
        float pitchAngle = (float)Math.acos(Vector3f.UNIT_X.dot(normal));
        rotation = TrackingHelper.getQuarternion(yawAngle, pitchAngle, rollAngle);*/
        
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
