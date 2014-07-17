/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.meshTraversal;

import org.zrd.geometryToolkit.meshDataStructure.MeshEdge;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import com.jme3.math.Matrix3f;
import com.jme3.math.Matrix4f;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import org.zrd.geometryToolkit.geometryUtil.ProgramConstants;

/**
 *
 * @author BLI
 */
public class TriangleLineSegmentIntersection {

    /*
     * Let the line segment be described by
     *      t*p + (1-t)q for two points p,q
     * The following floats are the values for t
     *      for each of the edges
     * If t is less than 0 or greater than 1, then
     *      we know there is no intersection
     */
    private float intersectEdge12,intersectEdge23,intersectEdge13;
    
    private MeshEdge ignoreEdge;
    
    private Vector3f lineStart,lineEnd;
    
    private float breakpointMag;
    
    private MeshTriangle currentTriangle;
    
    private int numBadEdges = 0;
    
    private boolean segDegenerate = false;
    
    public TriangleLineSegmentIntersection(MeshTriangle currentTri, 
            Vector3f lineSegmentStart, Vector3f lineSegmentEnd){
        
        lineStart = lineSegmentStart;
        lineEnd = lineSegmentEnd;
        
        this.currentTriangle = currentTri;

        //retrieves the vertices to use
       Vector3f vertex1 = currentTri.getVertex1().getVertex();
       Vector3f vertex2 = currentTri.getVertex2().getVertex();
       Vector3f vertex3 = currentTri.getVertex3().getVertex();
       
       /*
        * Translates all the points so that
        * vertex1 is now the origin
        */
       Matrix4f originVertex1 = MeshTraverseHelper.makeNewOrigin(vertex1);
       Vector3f seg12Vector = originVertex1.mult(vertex2);
       Vector3f seg13Vector = originVertex1.mult(vertex3);
       Vector3f lineSegStartUse = originVertex1.mult(lineSegmentStart);
       Vector3f lineSegEndUse = originVertex1.mult(lineSegmentEnd);
       
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
       lineSegStartUse.multLocal(localMultiplier);
       lineSegEndUse.multLocal(localMultiplier);
       
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
       Matrix3f coordMatrix = MeshTraverseHelper.getCoordinateTransformation(seg12Vector, seg13Vector, newZVector);
       Vector3f newStart = coordMatrix.mult(lineSegStartUse);
       Vector3f newEnd = coordMatrix.mult(lineSegEndUse);
       Vector3f newDir = newEnd.clone().subtract(newStart);
       
       /*
        * Finally, we use what we calculated above to easily get the t value
        *       for each of the intersection points. If the segment happens
        *       to be parallel to an edge, then we set the float value
        *       to infinity. 
        */
       intersectEdge12 = getIntersection(newStart.getY(),newDir.getY());
       if(Float.isInfinite(intersectEdge12)) numBadEdges++;
       intersectEdge13 = getIntersection(newStart.getX(),newDir.getX());
       if(Float.isInfinite(intersectEdge13)) numBadEdges++;
       Vector2f intersect23Points = MeshTraverseHelper.solveMatrixEqu(
               newDir.getX(), -1, 
               newDir.getY(), 1, 
               -1*newStart.getX(), 1-newStart.getY());
       intersectEdge23 = Float.POSITIVE_INFINITY;
       if(intersect23Points != null) intersectEdge23 = intersect23Points.getX();
       if(Float.isInfinite(intersectEdge23)) numBadEdges++;
       //else System.out.println("EDGE 23 WAS NULL!!!");
       if(numBadEdges>2){
           System.out.println("THREE BAD EDGES. DEGENERATE SEGMENT!");
           segDegenerate = true;
       }
        
    }

    public boolean isSegDegenerate() {
        return segDegenerate;
    }
    
    public MeshEdge getIntersectionEdge(MeshEdge edgeToIgnore){
        if(segDegenerate) return null;
        ignoreEdge = edgeToIgnore;
        if(isGoodEdge(intersectEdge12,currentTriangle.getSide12())){
            breakpointMag = intersectEdge12;
            return currentTriangle.getSide12();
        }
        if(isGoodEdge(intersectEdge13,currentTriangle.getSide13())){
            breakpointMag = intersectEdge13;
            return currentTriangle.getSide13();
        }
        if(isGoodEdge(intersectEdge23,currentTriangle.getSide23())){
            breakpointMag = intersectEdge23;
            return currentTriangle.getSide23();
        }
        return null;
    }
    
    public Vector3f getBreakpoint(){
        return getBreakpoint(breakpointMag);
    }
    
    public Vector3f getBreakpoint(float breakpoint){
        Vector3f deltaVector = getDeltaVector(breakpoint);
        return lineStart.add(deltaVector);
    }
    
    public Vector3f getDeltaVector(){
        return getDeltaVector(breakpointMag);
    }
    
    public Vector3f getDeltaVector(float breakpoint){
        Vector3f lineDir = lineEnd.subtract(lineStart);
        return lineDir.mult(breakpoint);
    }
    
    private boolean isGoodEdge(float edgeIntersectScaler,MeshEdge originalEdge){
        if(originalEdge.equals(ignoreEdge)){
            return false;
        }
        if(edgeIntersectScaler >= 0 && edgeIntersectScaler <= 1){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Used to help us calculate the intersection for edge 12 and 13
     * @param start
     * @param dir
     * @return 
     */
    private static float getIntersection(float start, float dir){
        float absDir = (float)Math.abs(dir);
        if(absDir<ProgramConstants.EPSILON){
            //System.out.println("DIR WAS NEAR ZERO!!!");
            return Float.POSITIVE_INFINITY;
        }else{
            return -1*start/dir;
        }
        
    }

    public float getIntersectEdge12() {
        return intersectEdge12;
    }

    public float getIntersectEdge23() {
        return intersectEdge23;
    }

    public float getIntersectEdge13() {
        return intersectEdge13;
    }
    
}
