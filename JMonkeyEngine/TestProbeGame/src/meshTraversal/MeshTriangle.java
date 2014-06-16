/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meshTraversal;

import com.jme3.math.Matrix4f;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import java.util.ArrayList;

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
    
    private boolean boundaryTriangle = false;
    
    public MeshTriangle(Triangle triangle, Matrix4f transform){
        vertices = new ArrayList<MeshVertex>(3);
        
        Vector3f vert1 = triangle.get1();
        Vector3f vert2 = triangle.get2();
        Vector3f vert3 = triangle.get3();
        
        Vector3f vert1Trans = transform.mult(vert1);
        Vector3f vert2Trans = transform.mult(vert2);
        Vector3f vert3Trans = transform.mult(vert3);
        
        vertex1 = new MeshVertex(vert1Trans);
        vertex2 = new MeshVertex(vert2Trans);
        vertex3 = new MeshVertex(vert3Trans);
        
        triangleData = new Triangle(vertex1.getVertex(),vertex2.getVertex(),vertex3.getVertex());
        
        side12 = new MeshEdge(vertex1,vertex2);
        side13 = new MeshEdge(vertex1,vertex3);
        side23 = new MeshEdge(vertex2,vertex3);

        vertices.add(vertex1);
        vertices.add(vertex2);
        vertices.add(vertex3);
    }
    
    public Vector3f getNormal(){
        triangleData.calculateNormal();
        return triangleData.getNormal();
    }

    public boolean isBoundaryTriangle() {
        return boundaryTriangle;
    }

    public void setBoundaryTriangle(boolean boundaryTriangle) {
        this.boundaryTriangle = boundaryTriangle;
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
    
    /**
     * This verifies that each of the edges were constructed
     * @return whether or not any of the edges are null
     */
    public boolean hasGoodEdges(){
        return (side12 != null) && (side23 != null) && (side13 != null);
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
        return MeshHelper.getTriangleInfo(triangleData);
    }
    
    
}
