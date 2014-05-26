/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meshTraversal;

import com.jme3.math.Triangle;

/**
 *
 * @author Zach
 */
public class MeshTriangle {
    
    
    private MeshEdge side12;
    private MeshEdge side13;
    private MeshEdge side23;
    
    private int triangleIndex;
    
    public MeshTriangle(Triangle triangle){
        triangleIndex = triangle.getIndex();
        
        side12 = new MeshEdge(triangle.get1(),triangle.get2());
        side13 = new MeshEdge(triangle.get1(),triangle.get3());
        side23 = new MeshEdge(triangle.get2(),triangle.get3());
    }

    public int getTriangleIndex() {
        return triangleIndex;
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
    
    
}
