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
public class TriangleSide {

    
    private Vector3f vertex1;
    private Vector3f vertex2;
    
    public TriangleSide(Vector3f vertex1, Vector3f vertex2){
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
        int hash = 7;
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
        final TriangleSide other = (TriangleSide) obj;
        if(this.vertex1 == other.vertex2 && this.vertex2 == other.vertex2){
            return true;
        }else if(this.vertex1 == other.vertex1 && this.vertex2 == other.vertex2){
            return true;
        }else{
            return false;
        }
    }
    
    
}
