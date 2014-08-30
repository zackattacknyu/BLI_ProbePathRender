/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.probeCalibration;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometricCalculations.TransformHelper;
import org.zrd.geometryToolkit.meshTraversal.PathOnMeshCalculator;
import org.zrd.geometryToolkit.pathDataStructure.SegmentSet;

/**
 *
 * This is used to test whether the displacement should be reflected
 *      over the x or y axis, meaning that the x or y scale factor
 *      should be negated.
 * 
 * It does this by taking in the x,y, and z vectors from the probe at the 
 *      initial position. It then takes in the starting vector v for the path 
 *      as well as the starting vector v' for the calibrated path.
 * It puts v and v' into the x,y,z coordinate system. It then sees 
 *      if v' is close to v reflected over x or y.
 * 
 * If v' is close to v reflected over x, then y should be negated
 * 
 * If v' is close to v reflected over y, then x should be negated
 * 
 * This test should be done after rotation calibration to determine
 *      if the x or y displacement vector should be flipped
 * 
 * @author BLI
 */
public class ReflectionCalibration {
    
    
    private float negatedXaccuracy;
    private float negatedYaccuracy;
    
    private boolean shouldNegateX;
    private boolean shouldNegateY;
    
    public static final float ACCURACY_REQUIRED_FOR_NEGATION_RECOMMENDATION = 0.5f;
    
    public ReflectionCalibration(Vector3f xVector, Vector3f yVector, Vector3f normalVector, 
            Vector3f originalVector, Vector3f modifiedVector){
        
        Matrix3f coordMatrix = TransformHelper.getCoordinateTransformation(xVector, yVector, normalVector);
        
        Vector2f originalXYVectorInNewCoords = TransformHelper.getXYVectorInNewCoords(coordMatrix, originalVector);
        Vector2f modifiedXYVectorInNewCoords = TransformHelper.getXYVectorInNewCoords(coordMatrix, modifiedVector);
        
        Vector2f modifiedVectorIfXNegated = originalXYVectorInNewCoords.clone();
        modifiedVectorIfXNegated.setX(originalXYVectorInNewCoords.getX()*-1);
        
        Vector2f modifiedVectorIfYNegated = originalXYVectorInNewCoords.clone();
        modifiedVectorIfYNegated.setY(originalXYVectorInNewCoords.getY()*-1);
        
        negatedXaccuracy = modifiedVectorIfXNegated.distance(modifiedXYVectorInNewCoords);
        negatedYaccuracy = modifiedVectorIfYNegated.distance(modifiedXYVectorInNewCoords);
        
        shouldNegateX = shouldRecommendNegation(negatedXaccuracy);
        shouldNegateY = shouldRecommendNegation(negatedYaccuracy);
    }
    
    public ReflectionCalibration(Vector3f xVector, Vector3f yVector, Vector3f normalVector, PathOnMeshCalculator rotCalib){
        this(xVector,yVector,normalVector,
                SegmentSet.getStartToEndUnitVector(rotCalib.getInitPath()),
                SegmentSet.getStartToEndUnitVector(rotCalib.getCurrentRotatedPath()));
    }
    
    public static boolean shouldRecommendNegation(float accuracy){
        return (accuracy<ACCURACY_REQUIRED_FOR_NEGATION_RECOMMENDATION);
    }

    /**
     * This simply gets the euclidean distance between the 
     *      modified vector and the vector if x was negated.
     * @return  accuracy of just negating x
     */
    public float getNegatedXaccuracy() {
        return negatedXaccuracy;
    }

    /**
     * This simply gets the euclidean distance between the 
     *      modified vector and the vector if y was negated.
     * @return  accuracy of just negating y
     */
    public float getNegatedYaccuracy() {
        return negatedYaccuracy;
    }

    public boolean shouldNegateX() {
        return shouldNegateX;
    }

    public boolean shouldNegateY() {
        return shouldNegateY;
    }
    
    public ArrayList<String> getResults(){
        
        ArrayList<String> results = new ArrayList<>(4);
        
        results.add("Negated X accuracy: " + negatedXaccuracy);
        results.add("Should negate X: " + shouldNegateX);
        
        results.add("Negated Y accuracy: " + negatedYaccuracy);
        results.add("Should negate Y: " + shouldNegateY);
        
        return results;
    }
}
