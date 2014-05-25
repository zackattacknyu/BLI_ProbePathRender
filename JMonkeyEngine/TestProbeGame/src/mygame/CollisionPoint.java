/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.collision.CollisionResult;
import com.jme3.math.Quaternion;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;

/**
 *
 * @author BLI
 */
public class CollisionPoint {
    
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
        return triangle.get(0).toString() + "," 
                + triangle.get(1).toString() + 
                "," + triangle.get(2).toString();
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
