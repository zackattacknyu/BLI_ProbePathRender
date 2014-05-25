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
    
    
    private Triangle triangleData;
    
    //stores pointers to neighboring triangles
    private MeshTriangle v12Triangle;
    private MeshTriangle v23Triangle;
    private MeshTriangle v13Triangle;
    
    private TriangleSide side12;
    private TriangleSide side13;
    private TriangleSide side23;
    
    public MeshTriangle(Triangle triangle){
        triangleData = triangle;
        
        side12 = new TriangleSide(triangle.get1(),triangle.get2());
        side13 = new TriangleSide(triangle.get2(),triangle.get3());
        side23 = new TriangleSide(triangle.get2(),triangle.get3());
    }

    public void setV12Triangle(MeshTriangle v12Triangle) {
        this.v12Triangle = v12Triangle;
    }

    public void setV23Triangle(MeshTriangle v23Triangle) {
        this.v23Triangle = v23Triangle;
    }

    public void setV13Triangle(MeshTriangle v13Triangle) {
        this.v13Triangle = v13Triangle;
    }

    public TriangleSide getSide12() {
        return side12;
    }

    public TriangleSide getSide13() {
        return side13;
    }

    public TriangleSide getSide23() {
        return side23;
    }
    
    
}
