/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.pathDataStructure;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 *
 * @author BLI
 */
public class SegmentData {
    
    
    private Vector3f vertex;
    private Vector2f textureCoord;
    private long timestamp = 0;
    private String[] data;
    
    public SegmentData(Vector3f vertex, Vector2f textureCoord, String[] data, long timestamp){
        this(vertex,data,timestamp);
        this.textureCoord = textureCoord;
    }
    
    public SegmentData(Vector3f vertex){
        this.vertex = vertex;
    }
    
    public SegmentData(Vector3f vertex, Vector2f textureCoord){
        this.vertex = vertex;
        this.textureCoord = textureCoord;
    }
    
    public SegmentData(Vector3f vertex, String[] data, long timestamp){
        this(vertex,data);
        this.timestamp = timestamp;
    }

    public Vector2f getTextureCoord() {
        return textureCoord;
    }

    public SegmentData(Vector3f vertex, String[] data) {
        this.vertex = vertex;
        this.data = data;
    }
    
    public long getTimestamp(){
        return timestamp;
    }

    public Vector3f getVertex() {
        return vertex;
    }

    public String[] getData() {
        return data;
    }
    
    
    
    
    
}
