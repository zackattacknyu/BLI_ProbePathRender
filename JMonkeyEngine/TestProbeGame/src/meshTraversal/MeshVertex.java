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
    
    private static final float errorThreshold = (float) Math.pow(10, -4);
    
    public MeshVertex(Vector3f vertex){
        this.vertex = vertex;
    }

    public Vector3f getVertex() {
        return vertex;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (this.vertex != null ? this.vertex.hashCode() : 0);
        return hash;
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
        
        float xDiff = Math.abs(other.getVertex().getX() - vertex.getX());
        float yDiff = Math.abs(other.getVertex().getY() - vertex.getY());
        float zDiff = Math.abs(other.getVertex().getZ() - vertex.getZ());
        
        if(xDiff < errorThreshold && yDiff < errorThreshold && zDiff < errorThreshold){
            return true;
        }else{
            return false;
        }
        
        
    }
    
    
    
}
