/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.meshTraversal;

import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import com.jme3.math.Matrix3f;
import com.jme3.math.Matrix4f;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import org.zrd.geometryToolkit.geometricCalculations.TransformHelper;
import org.zrd.geometryToolkit.geometricCalculations.TranslationHelper;
import org.zrd.geometryToolkit.meshDataStructure.TriangleTexture;

/**
 *
 * @author BLI
 */
public class TriangleTextureCoord {

    private Vector2f textureCoordinate;
    
    public TriangleTextureCoord(MeshTriangle currentTri, 
            Vector3f originalPointCoord){

        //retrieves the vertices to use
       Vector3f vertex1 = currentTri.getVertex1().getVertex();
       Vector3f vertex2 = currentTri.getVertex2().getVertex();
       Vector3f vertex3 = currentTri.getVertex3().getVertex();
       
       /*
        * Translates all the points so that
        * vertex1 is now the origin
        */
       Matrix4f originVertex1 = TranslationHelper.getNewOriginTransform(vertex1);
       Vector3f seg12Vector = originVertex1.mult(vertex2);
       Vector3f seg13Vector = originVertex1.mult(vertex3);
       Vector3f pointCoordToUse = originVertex1.mult(originalPointCoord);
       
       /*
        * This part is necessary due to rounding errors skewing
        *       our results. By multplying, we are ensuring
        *       that rounding errors won't affect the final result.
        * Additionally, the t value won't be affected by translation, rotation,
        *       or scaling, so this has no impact on the final result
        */
       float localMultiplier = (float)Math.pow(10, 5);
       seg12Vector.multLocal(localMultiplier);
       seg13Vector.multLocal(localMultiplier);
       pointCoordToUse.multLocal(localMultiplier);
       
       /*
        * This finds the vector perpendicular to 12 and 13. 
        *   It is used as the third vector in the coordinate transformation.
        *   Its component should be less than epsilon
        */
       Vector3f newZVector = seg12Vector.clone().cross(seg13Vector.clone());

       /*
        * This is the important part. It transforms the points to a coordinate
        *       system where the two important basis vector are the 12 edge and
        *       the 13 edge. This transformation allows us to easily find all the
        *       t values that we need. 
        */
       Matrix3f coordMatrix = TransformHelper.getCoordinateTransformation(seg12Vector, seg13Vector, newZVector);
       Vector3f pointCoordInNewSystem = coordMatrix.mult(pointCoordToUse);
       
       Vector2f pointCoordFlat = new Vector2f(pointCoordInNewSystem.getX(),pointCoordInNewSystem.getY());
       
       //get the current texture coordinates
       TriangleTexture texCoords = currentTri.getTextureCoords();
       
       if(texCoords == null){
           textureCoordinate = new Vector2f(Float.NaN,Float.NaN);
           return;
       }
       
       Vector2f vertex1tex = texCoords.getVertex1texCoord();
       Vector2f vertex2tex = texCoords.getVertex2texCoord();
       Vector2f vertex3tex = texCoords.getVertex3texCoord();
       
       //gets the 12 and 13 vectors
       Vector2f seg12texVector = vertex2tex.subtract(vertex1tex);
       Vector2f seg13texVector = vertex3tex.subtract(vertex1tex);
       
       //gets the point coord as vector from vertex 1 as origin
       Vector2f pointCoordFlatVector = seg12texVector.mult(pointCoordFlat.getX()).add(
               seg13texVector.mult(pointCoordFlat.getY()));
       textureCoordinate = pointCoordFlatVector.add(vertex1tex);
    }

    public Vector2f getTextureCoordinate() {
        return textureCoordinate;
    }
    
    

    
}
