/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.graphicsTools.geometry.mesh;

import org.zrd.graphicsTools.geometry.mesh.MeshTriangle;
import org.zrd.graphicsTools.geometry.mesh.TriangleSet;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 *
 * @author BLI
 */
public class ConnectedComponent {

    private TriangleSet parentTriangleSet;
    private TriangleSet componentTriangleSet;
    private TriangleSet remainingTriangleSet;
    private Set<MeshTriangle> componentTriangles;
    private Set<MeshTriangle> remainingTriangles;
    
    public ConnectedComponent(TriangleSet triangles, MeshTriangle seedTriangle){
        this.parentTriangleSet = triangles;
        componentTriangles = new HashSet<MeshTriangle>(triangles.getTriangleList().size());
        
        initRemainingTriangles();
        
        constructComponentFromSeed(seedTriangle);
        
        //viewRemainingTriangles();
        
        constructTriangleSets();
    }

    public TriangleSet getComponentTriangleSet() {
        return componentTriangleSet;
    }

    public TriangleSet getRemainingTriangleSet() {
        return remainingTriangleSet;
    }
    
    private void initRemainingTriangles(){
        /*
         * In case they are not all in a connected component, this tells 
         *      us the parentTriangleSet that are left
         */
        remainingTriangles = new HashSet<MeshTriangle>(parentTriangleSet.getTriangleList().size());
        for(MeshTriangle triangle: parentTriangleSet.getTriangleList()){
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
     * This works by maintaining a stack of parentTriangleSet to be visited. Once
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
        Set<MeshTriangle> visitedTriangles = new HashSet<MeshTriangle>();
        trianglesToVisit.push(currentTriangle);
        while(!trianglesToVisit.isEmpty()){
            currentTriangle = trianglesToVisit.pop();
            visitedTriangles.add(currentTriangle);
            
            componentTriangles.add(currentTriangle);
            remainingTriangles.remove(currentTriangle);

            for(MeshTriangle triangle: parentTriangleSet.getEdgeNeighbors(currentTriangle)){
                if(!visitedTriangles.contains(triangle)){
                    trianglesToVisit.push(triangle);
                }
            }
        }
    }

    public Set<MeshTriangle> getComponentTriangles() {
        return componentTriangles;
    }

    private void constructTriangleSets() {
        componentTriangleSet = constructTriangleSet(componentTriangles);
        remainingTriangleSet = constructTriangleSet(remainingTriangles);
    }
    
    public static TriangleSet constructTriangleSet(Set<MeshTriangle> triangles){
        TriangleSet newTriSet = new TriangleSet();
        for(MeshTriangle meshTri: triangles){
            newTriSet.addTriangle(meshTri);
        }
        return newTriSet;
    }
    
}
