/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelVerifier;

import java.util.ArrayList;
import meshTraversal.ConnectedComponent;
import meshTraversal.TriangleSet;

/**
 *
 * @author BLI
 */
public class ModelCorrection {

    
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
