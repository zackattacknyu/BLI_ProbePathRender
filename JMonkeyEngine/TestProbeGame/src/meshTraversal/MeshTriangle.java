/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meshTraversal;

import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Zach
 */
public class MeshTriangle {
    
    
    private MeshEdge side12;
    private MeshEdge side13;
    private MeshEdge side23;
    
    private MeshVertex vertex1,vertex2,vertex3;
    
    private Triangle triangleData;
    
    private ArrayList<MeshVertex> vertices;
    
    public MeshTriangle(Triangle triangle){
        triangleData = triangle;
        vertices = new ArrayList<MeshVertex>(3);
        
        side12 = new MeshEdge(triangle.get1(),triangle.get2());
        side13 = new MeshEdge(triangle.get1(),triangle.get3());
        side23 = new MeshEdge(triangle.get2(),triangle.get3());
        
        vertex1 = new MeshVertex(triangleData.get1());
        vertex2 = new MeshVertex(triangleData.get2());
        vertex3 = new MeshVertex(triangleData.get3());
        
        vertices.add(vertex1);
        vertices.add(vertex2);
        vertices.add(vertex3);
    }

    public MeshEdge getSide12() {
        return side12;
    }

    public MeshEdge getSide13() {
        return side13;
    }

    public MeshEdge getSide23() {
        return side23;
    }

    public MeshVertex getVertex1() {
        return vertex1;
    }

    public MeshVertex getVertex2() {
        return vertex2;
    }

    public MeshVertex getVertex3() {
        return vertex3;
    }

    public ArrayList<MeshVertex> getVertices() {
        return vertices;
    }

    @Override
    public int hashCode() {
        return vertex1.hashCode() 
                + vertex2.hashCode() + 
                vertex3.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MeshTriangle other = (MeshTriangle) obj;
        
        return other.getVertices().containsAll(vertices);
        
    }

    @Override
    public String toString() {
        return triangleData.get(0).toString() + "," 
                + triangleData.get(1).toString() + 
                "," + triangleData.get(2).toString();
    }
    
    
}
