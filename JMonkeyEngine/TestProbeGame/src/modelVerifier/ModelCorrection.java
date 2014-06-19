/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelVerifier;

import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import meshTraversal.ConnectedComponent;
import meshTraversal.MeshTriangle;
import meshTraversal.TriangleSet;

/**
 *
 * @author BLI
 */
public class ModelCorrection {
    
    public static TriangleSet getSmoothedTriangleSet(TriangleSet triangles){
        Vector3f baseNormal;
        Vector3f currentNormal;
        
        TriangleSet newTriSet = new TriangleSet();

        Set<MeshTriangle> addedTriangles = new HashSet<MeshTriangle>(triangles.getTriangleList().size());
        MeshTriangle reformedTriangle;
        
        for(MeshTriangle baseTriangle : triangles.getTriangleList()){
            baseNormal = baseTriangle.getNormal();
            for(MeshTriangle currentTriangle : triangles.getEdgeNeighbors(baseTriangle)){
                
                currentNormal = currentTriangle.getNormal();
                
                if(!addedTriangles.contains(currentTriangle)){
                    
                    if(!ModelVerification.verifyOutwardDir(currentNormal,baseNormal)){
                        currentTriangle.reorderVertices();   
                    }
                    newTriSet.addTriangle(currentTriangle);
                    addedTriangles.add(currentTriangle);
                }
                
            }
        }
        
        return newTriSet;
    }

    
    public static ConnectedComponent getLargestComponent(TriangleSet triangles){
        ArrayList<ConnectedComponent> components = new ArrayList<ConnectedComponent>(5);
        TriangleSet currentTriSet = triangles;
        ConnectedComponent currentComp;
        while(!currentTriSet.getTriangleList().isEmpty()){
            currentComp = new ConnectedComponent(currentTriSet,currentTriSet.getTriangleList().get(0));
            components.add(currentComp);
            currentTriSet = currentComp.getRemainingTriangleSet();
        }
        
        int maxConnectedComponents = 0;
        int currentSize;
        
        currentComp = components.get(0);
        for(int ind = 0; ind < components.size(); ind++){
            currentSize = components.get(ind).getComponentTriangles().size();
            if(currentSize > maxConnectedComponents){
                maxConnectedComponents = currentSize;
                currentComp = components.get(ind);
            }
        }
        
        System.out.println("Largest Component has " + currentComp.getComponentTriangles().size() + " triangles");
        
        return currentComp;
    }
    
}
