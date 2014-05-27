/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meshTraversal;

/**
 *
 * @author Zach
 */
public class MeshEdgeTriangles {
    
    private MeshTriangle triangle1;
    private MeshTriangle triangle2;
    
    public MeshEdgeTriangles(){
        triangle1 = null;
        triangle2 = null;
    }
    
    public void addTriangle(MeshTriangle triangle){
        if(triangle1 == null){
            triangle1 = triangle;
        }else if(triangle2 == null && !triangle.equals(triangle1)){
            triangle2 = triangle;
        }
    }
    
    public MeshTriangle getOtherTriangle(MeshTriangle currentTriangle){
        if(currentTriangle.equals(triangle1)){
            return triangle2;
        }else{
            return triangle1;
        }
    }

    @Override
    public String toString() {
        return "MeshEdgeTriangles{" + "triangle1=" + triangle1 + ", triangle2=" + triangle2 + '}';
    }
    
}