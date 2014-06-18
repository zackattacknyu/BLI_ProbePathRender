/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelVerifier;

import com.jme3.math.Vector3f;
import java.util.HashSet;
import java.util.Set;
import meshTraversal.ConnectedComponent;
import meshTraversal.MeshEdge;
import meshTraversal.MeshEdgeTriangles;
import meshTraversal.MeshTriangle;
import meshTraversal.TriangleSet;
import mygame.Constants;

/**
 *
 * @author BLI
 */
public class ModelVerification {
    
    private static final Vector3f maxXbase = Vector3f.UNIT_X;
    private static final Vector3f maxYbase = Vector3f.UNIT_Y;
    private static final Vector3f maxZbase = Vector3f.UNIT_Z;
    
    private static final Vector3f minXbase = maxXbase.negate();
    private static final Vector3f minYbase = maxYbase.negate();
    private static final Vector3f minZbase = maxZbase.negate();
    
    private static final Vector3f[] baseNormals = {
        minXbase, maxXbase, minYbase, maxYbase, minZbase, maxZbase
    };
    
    public static void performModelVerification(TriangleSet triangles){
        boolean numEdgesPerTriangle = verifyNumEdgesPerTriangle(triangles);
        boolean numTrianglesPerEdge = verifyNumTrianglesPerEdge(triangles);
        boolean singleComponent = singleConnectedComponent(triangles);
        boolean noDegenTriangles = verifyNoDegenerateTriangles(triangles);
        boolean boundingNormalsOutward = verifyOutwardBoundingNormals(triangles);
        boolean smoothNormals = verifySmoothNormals(triangles);
        
        System.out.println();
        System.out.println("Now Running Model Verification:");
        System.out.println("3 Edges Exist for each triangle: " + numEdgesPerTriangle);
        System.out.println("2 Triangles Per Edge unless boundary: " + numTrianglesPerEdge);
        System.out.println("Mesh is Single Connected Component: " + singleComponent);
        System.out.println("No Degenerate Triangles: " + noDegenTriangles);
        System.out.println("Bounding Vertex Normals Point Outward: " + boundingNormalsOutward);
        System.out.println("Smooth Normals: " + smoothNormals);
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
    
    /**
     * This runs the verification that every triangle is not degenerate:
     *      - Two vertices cannot be the same
     *      - Points cannot be collinear
     * @param triangles
     * @return whether or not each triangle has 3 distinct vertices
     */
    public static boolean verifyNoDegenerateTriangles(TriangleSet triangles){
        Vector3f vector12,vector13;
        float dotProd;
        for(MeshTriangle triangle:triangles.getTriangleList()){
            vector12 = triangle.getSide12().getVector();
            vector13 = triangle.getSide13().getVector();
            
            //degenerate side
            if( (vector12.length() < Constants.EPSILON) 
                    || (vector13.length() < Constants.EPSILON) ){
                System.out.println("Degen Triangle: " + triangle);
                return false;
            }
            
            //collinear
            vector12.normalizeLocal();
            vector13.normalizeLocal();
            dotProd = vector12.dot(vector13);
            if((Math.abs(dotProd-1) < Constants.EPSILON) || 
                    (Math.abs(dotProd+1) < Constants.EPSILON)){
                //if dot prod is near 1 or -1, then unit vectors are identical
                System.out.println("Degen Triangle: " + triangle);
                System.out.println("Degen Triangle texture coordinates: " + triangle.getTextureCoords());
                return false;
            }
            
        }
        return true;
    }

    private static boolean verifyOutwardBoundingNormals(TriangleSet triangles) {
        Vector3f[] normalsToCheck = {
            triangles.getNormalAtVertex(triangles.getVertexWithMinX()),
            triangles.getNormalAtVertex(triangles.getVertexWithMaxX()),
            triangles.getNormalAtVertex(triangles.getVertexWithMinY()),
            triangles.getNormalAtVertex(triangles.getVertexWithMaxY()),
            triangles.getNormalAtVertex(triangles.getVertexWithMinZ()),
            triangles.getNormalAtVertex(triangles.getVertexWithMaxZ())
        };
        
        for(int index = 0; index < 6; index++){
            if(!verifyOutwardDir(normalsToCheck[index],baseNormals[index])){
                System.out.println("Normal at vertex " + index + ": " + 
                        normalsToCheck[index] + " where base is " + 
                        baseNormals[index]);
                return false;
            }
        }

        return true;
    }
    
    private static boolean verifyOutwardDir(Vector3f dirToCheck, Vector3f currentOutwardDir){
        return dirToCheck.normalize().dot(currentOutwardDir.normalize()) > 0;
    }

    private static boolean verifySmoothNormals(TriangleSet triangles) {
        Vector3f baseNormal;
        Vector3f currentNormal;
        
        for(MeshTriangle baseTriangle : triangles.getTriangleList()){
            baseNormal = baseTriangle.getNormal();
            for(MeshTriangle currentTriangle : triangles.getEdgeNeighbors(baseTriangle)){
                currentNormal = currentTriangle.getNormal();
                if(!verifyOutwardDir(currentNormal,baseNormal)){
                    System.out.println("Bad Adjacent Normals: " + currentNormal + " and " + baseNormal);
                    System.out.println("Normal 1 Texture Coords: " + currentTriangle.getTextureCoords());
                    System.out.println("Normal 2 Texture Coords: " + baseTriangle.getTextureCoords());
                    return false;
                }
            }
        }
        
        return true;
    }
}
