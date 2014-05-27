/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meshTraversal;

import com.jme3.math.Vector3f;

/**
 *
 * @author Zach
 */
public class MeshVertex {

    private Vector3f vertex;
    
    
    
    public MeshVertex(Vector3f vertex){
        this.vertex = vertex;
    }

    public Vector3f getVertex() {
        return vertex;
    }

    @Override
    public int hashCode() {
        return vertex.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MeshVertex other = (MeshVertex) obj;

        return other.getVertex().equals(vertex);
        
        
    }

    @Override
    public String toString() {
        return String.valueOf(vertex);
    }
    
    
    
}
