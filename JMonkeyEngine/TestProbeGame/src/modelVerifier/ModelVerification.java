/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelVerifier;

import java.util.HashSet;
import java.util.Set;
import meshTraversal.ConnectedComponent;
import meshTraversal.MeshEdge;
import meshTraversal.MeshEdgeTriangles;
import meshTraversal.MeshTriangle;
import meshTraversal.TriangleSet;

/**
 *
 * @author BLI
 */
public class ModelVerification {
    
    public static void performModelVerification(TriangleSet triangles){
        boolean numEdgesPerTriangle = verifyNumEdgesPerTriangle(triangles);
        boolean numTrianglesPerEdge = verifyNumTrianglesPerEdge(triangles);
        boolean singleComponent = singleConnectedComponent(triangles);
        
        System.out.println();
        System.out.println("Now Running Model Verification:");
        System.out.println("3 Edges Exist for each triangle: " + numEdgesPerTriangle);
        System.out.println("2 Triangles Per Edge unless boundary: " + numTrianglesPerEdge);
        System.out.println("Mesh is Single Connected Component: " + singleComponent);
    }
    
    /**
     * This verifies that the triangles are a single connected component. It works
     *      by recursively travelling through all the neighbors of a triangle
     *      and verifying that the number of triangles added is the total number
     *      of triangles that we have
     * @param triangles
     * @return 
     */
    public static boolean singleConnectedComponent(TriangleSet triangles){
        
        MeshTriangle startingTriangle = triangles.getTriangleList().get(0);
        ConnectedComponent currentComp = new ConnectedComponent(triangles,startingTriangle);
        
        int numTrianglesComponent = currentComp.getComponentTriangles().size();
        int numTrianglesMesh = triangles.getTriangleList().size();
        
        System.out.println("Component has " + numTrianglesComponent + " Triangles");
        System.out.println("Mesh has " + numTrianglesMesh + " Triangles");
        
        return (numTrianglesComponent==numTrianglesMesh);
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
                System.out.println("LONE EDGE!!");
                return false;
            }else if(currentTriangles.numTriangles() < 2){
                
                //if edge has 1 triangle, this makes sure it was correctly labelled as such
                if(!currentTriangles.getTriangle1().isBoundaryTriangle()){
                    System.out.println("UNLABELLED BOUNDARY TRIANGLE!!");
                    return false;
                }
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
