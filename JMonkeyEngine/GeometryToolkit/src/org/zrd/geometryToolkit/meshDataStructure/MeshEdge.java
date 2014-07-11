/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.meshDataStructure;

import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 *
 * @author Zach
 */
public class MeshEdge {
    
    private MeshVertex vertex1;
    private MeshVertex vertex2;
    private ArrayList<MeshVertex> vertices;
    
    public MeshEdge(Vector3f vertex1, Vector3f vertex2){
        this(new MeshVertex(vertex1), new MeshVertex(vertex2));
    }
    
    public MeshEdge(MeshVertex vertex1, MeshVertex vertex2){
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        vertices = new ArrayList<MeshVertex>(2);
        vertices.add(this.vertex1);
        vertices.add(this.vertex2);
    }

    public MeshVertex getVertex1() {
        return vertex1;
    }

    public MeshVertex getVertex2() {
        return vertex2;
    }
    
    public Vector3f getVector(){
        return vertex2.getVertex().subtract(vertex1.getVertex());
    }

    public ArrayList<MeshVertex> getVertices() {
        return vertices;
    }

    @Override
    public int hashCode() {
        return vertex1.hashCode() + vertex2.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MeshEdge other = (MeshEdge) obj;
        
        return vertices.containsAll(other.getVertices());
    }

    @Override
    public String toString() {
        return "MeshEdge{" + "vertex1=" + vertex1 + ", vertex2=" + vertex2 + '}';
    }
    
}
