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
public class MeshEdge {
    
    private Vector3f vertex1;
    private Vector3f vertex2;
    
    public MeshEdge(Vector3f vertex1, Vector3f vertex2){
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
    }

    public Vector3f getVertex1() {
        return vertex1;
    }

    public Vector3f getVertex2() {
        return vertex2;
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
        if(this.vertex1.equals(other.getVertex2()) && 
                this.vertex2.equals(other.getVertex1())){
            return true;
        }else if(this.vertex1.equals(other.getVertex1()) && 
                this.vertex2.equals(other.getVertex2())){
            return true;
        }else{
            return false;
        }
    }
    
}
