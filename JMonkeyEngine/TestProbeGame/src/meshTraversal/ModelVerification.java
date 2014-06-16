/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meshTraversal;

/**
 *
 * @author BLI
 */
public class ModelVerification {
    
    public static void performModelVerification(TriangleSet triangles){
        boolean numEdgesPerTriangle = verifyNumEdgesPerTriangle(triangles);
        boolean numTrianglesPerEdge = verifyNumTrianglesPerEdge(triangles);
        
        System.out.println();
        System.out.println("Now Running Model Verification:");
        System.out.println("3 Edges Exist for each triangle: " + numEdgesPerTriangle);
        System.out.println("2 Triangles Per Edge unless boundary: " + numTrianglesPerEdge);
    }
    
    /**
     * This verifies that each edge has 2 triangles unless it's a boundary edge
     * @param triangles the triangle set
     * @return 
     */
    public static boolean verifyNumTrianglesPerEdge(TriangleSet triangles){
        MeshEdgeTriangles currentTriangles;
        for(MeshEdge edge: triangles.getTrianglesByEdge().keySet()){
            currentTriangles = triangles.getTrianglesByEdge().get(edge);
            if(currentTriangles.numTriangles() < 1){
                
                //if lone edge, this is not a clean model
                return false;
            }else if(currentTriangles.numTriangles() < 2){
                
                //if edge has 1 triangle, this makes sure it was correctly labelled as such
                return currentTriangles.getTriangle1().isBoundaryTriangle();
            }
        }
        return true;
    }

    /**
     * This runs the verification that every triangle has 3 edges
     * @param triangles
     * @return whether or not each triangle has 3 edges
     */
    public static boolean verifyNumEdgesPerTriangle(TriangleSet triangles){
        for(MeshTriangle triangle:triangles.getTriangleList()){
            if(!triangle.hasGoodEdges()) return false;
        }
        return true;
    }
}
