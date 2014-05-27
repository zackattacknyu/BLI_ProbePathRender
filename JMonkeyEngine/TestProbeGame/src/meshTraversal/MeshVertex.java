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
    
    private static final int thresholdPower = -5;
    
    private static final float errorThreshold = (float) Math.pow(10, thresholdPower);
    private static final double hashMultiplier = Math.pow(10, 6);
    
    
    
    public MeshVertex(Vector3f vertex){
        this.vertex = vertex;
    }

    public Vector3f getVertex() {
        return vertex;
    }

    @Override
    public int hashCode() {
        //double initHash = (vertex.getX() + vertex.getY() + vertex.getZ())*hashMultiplier;
        return vertex.hashCode();
        //return (int)(Math.round(initHash));
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
        
        /*float xDiff = Math.abs(other.getVertex().getX() - vertex.getX());
        float yDiff = Math.abs(other.getVertex().getY() - vertex.getY());
        float zDiff = Math.abs(other.getVertex().getZ() - vertex.getZ());
        
        if(xDiff < errorThreshold && yDiff < errorThreshold && zDiff < errorThreshold){
            return true;
        }else{
            return false;
        }*/
        //return other.getVertex().toString().equals(vertex.toString());
        return other.getVertex().equals(vertex);
        
        
    }

    @Override
    public String toString() {
        return String.valueOf(vertex);
    }
    
    
    
}
