/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.meshDataStructure;

import com.jme3.math.Vector2f;

/**
 *
 * @author BLI
 */
public class TriangleTexture {
    
    private Vector2f vertex1texCoord;
    private Vector2f vertex2texCoord;
    private Vector2f vertex3texCoord;
    
    public TriangleTexture(Vector2f vertex1texCoord,Vector2f vertex2texCoord,Vector2f vertex3texCoord){
        this.vertex1texCoord = vertex1texCoord;
        this.vertex2texCoord = vertex2texCoord;
        this.vertex3texCoord = vertex3texCoord;
    }

    public Vector2f getVertex1texCoord() {
        return vertex1texCoord;
    }

    public Vector2f getVertex2texCoord() {
        return vertex2texCoord;
    }

    public Vector2f getVertex3texCoord() {
        return vertex3texCoord;
    }

    @Override
    public String toString() {
        return vertex1texCoord + " , " + vertex2texCoord + " , " + vertex3texCoord + '}';
    }
    
    
    
}
