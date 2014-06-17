/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meshTraversal;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 *
 * @author BLI
 */
public class ConnectedComponent {

    private TriangleSet triangles;
    private Set<MeshTriangle> componentTriangles;
    private Set<MeshTriangle> remainingTriangles;
    
    public ConnectedComponent(TriangleSet triangles, MeshTriangle seedTriangle){
        this.triangles = triangles;
        componentTriangles = new HashSet<MeshTriangle>(triangles.getTriangleList().size());
        
        initRemainingTriangles();
        
        constructComponentFromSeed(seedTriangle);
        
        viewRemainingTriangles();
    }
    
    private void initRemainingTriangles(){
        /*
         * In case they are not all in a connected component, this tells 
         *      us the triangles that are left
         */
        remainingTriangles = new HashSet<MeshTriangle>(triangles.getTriangleList().size());
        for(MeshTriangle triangle: triangles.getTriangleList()){
            remainingTriangles.add(triangle);
        }
    }
    
    private void viewRemainingTriangles(){
        System.out.println("Triangles not in component: ");
        for(MeshTriangle triangle: remainingTriangles){
            System.out.println(triangle);
        }
    }
    
    /**
     * This constructs the connected component from a seed triangle
     * This works by maintaining a stack of triangles to be visited. Once
     *      a triangle is visited, all its unvisited neighbors get added to the
     *      stack.
     * 
     * **IMPORTANT WARNING!!!!!!!**
     * DO NOT CHANGE THIS METHOD TO BE RECURSIVE INSTEAD:
     *      If you use recursion here to go through each triangle,
     *      a StackOverflowError will result.
     * 
     * @param seedTriangle 
     */
    private void constructComponentFromSeed(MeshTriangle seedTriangle){
        MeshTriangle currentTriangle = seedTriangle;
        Stack<MeshTriangle> trianglesToVisit = new Stack<MeshTriangle>();
        trianglesToVisit.push(currentTriangle);
        while(!trianglesToVisit.isEmpty()){
            currentTriangle = trianglesToVisit.pop();
            
            //in case a triangle was added to the stack twice
            if(!componentTriangles.contains(currentTriangle)){
                
                componentTriangles.add(currentTriangle);
                remainingTriangles.remove(currentTriangle);
                
                for(MeshTriangle triangle: triangles.getEdgeNeighbors(currentTriangle)){
                    if(!componentTriangles.contains(triangle)){
                        trianglesToVisit.push(triangle);
                    }
                }

            }
        }
    }

    public Set<MeshTriangle> getComponentTriangles() {
        return componentTriangles;
    }
    
}
