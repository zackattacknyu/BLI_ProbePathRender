/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meshTraversal;

import com.jme3.math.Triangle;
import com.jme3.scene.Mesh;

/**
 *
 * @author Zach
 */
public class TriangleSet {

    Mesh mesh;
    
    private MeshTriangle[] triangles;
    
   public TriangleSet(Mesh mesh){
       triangles = new MeshTriangle[mesh.getTriangleCount()];
       Triangle newTri = new Triangle();
       MeshTriangle currentTri;
       MeshTriangle currentPotentialNeighbor;
       
       //put all triangles into an array
       for(int index = 0; index < triangles.length; index++){
           mesh.getTriangle(index, newTri);
           triangles[index] = new MeshTriangle(newTri);
       }
       
       //find all the neighbors
       for(int index = 0; index < triangles.length; index++){
           currentTri = triangles[index];
           for(int otherInd = 0; otherInd < triangles.length; otherInd++){
               currentPotentialNeighbor = triangles[otherInd];

               if(currentTri.getSide12().equals(currentPotentialNeighbor.getSide12())
                       || currentTri.getSide12().equals(currentPotentialNeighbor.getSide23()) ||
                       currentTri.getSide12().equals(currentPotentialNeighbor.getSide13())){
                   currentTri.setV12Triangle(currentPotentialNeighbor);
               }
               if(currentTri.getSide13().equals(currentPotentialNeighbor.getSide12())
                       || currentTri.getSide13().equals(currentPotentialNeighbor.getSide23()) ||
                       currentTri.getSide13().equals(currentPotentialNeighbor.getSide13())){
                   currentTri.setV13Triangle(currentPotentialNeighbor);
               }
               if(currentTri.getSide23().equals(currentPotentialNeighbor.getSide12())
                       || currentTri.getSide23().equals(currentPotentialNeighbor.getSide23()) ||
                       currentTri.getSide23().equals(currentPotentialNeighbor.getSide13())){
                   currentTri.setV23Triangle(currentPotentialNeighbor);
               }
           }
       }
   }
    
}
