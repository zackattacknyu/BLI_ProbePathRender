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
        
        System.out.println();
        System.out.println("Now Running Model Verification:");
        System.out.println("3 Edges Exist for each triangle: " + numEdgesPerTriangle);
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
