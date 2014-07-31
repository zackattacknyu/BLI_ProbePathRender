/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.meshTraversal;

import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometricCalculations.AngleAxisRotation;
import org.zrd.geometryToolkit.geometricCalculations.MathHelper;
import org.zrd.geometryToolkit.geometricCalculations.RotationTransformHelper;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.pathTools.PathHelper;
import org.zrd.geometryToolkit.pathTools.PathTransformHelper;

/**
 * ALGORITHMIC CODE:
 * This implements my algorithm for taking a path and transforming
 *      it onto the mesh. 
 * 
 * This houses the code that takes in a mesh and a path
 *      as well as the target start and end points
 *      on the mesh and tries to make the path follow 
 *      the mesh so that the end points match
 * 
 * To find the correct transform, we need to rotate the path so the starting
 *      segment is on the starting triangle and then rotate it along
 *      that triangle until the end points match up. Due to problems that can 
 *      occur with mesh flattening, I did not try for a deterministic solution
 *      to this problem and instead have an approximation algorithm which does
 *      a binary search to try and find that second rotation along the 
 *      first triangle. 
 * 
 * Here is the approximation algorithm:
 *      1. Rotate the path so that the first segment is on the first triangle
 *      2. Make the first triangle normal N and rotated path P and P'
 *      3. Take the end point of P', the start point, and the target end point
 *              and find the rotation R so that the path end point will match
 *              up the target end point
 *      4. Convert R to angle and axis rotation and label its angle Theta
 *      5. Find the rotation R' that consists of the axis N with an angle of Theta
 *      6. Rotate the path P by R' and make it path P
 *      7. Project path P onto the mesh to get path P'
 *      8. Find the distance between the target end point and the end point of P'
 *      9. Repeat steps 3-8 until the distances in step 8 converge to a value
 * 
 * It is important to note that we do not keep track of paths as we go
 *      but rather we keep track of the aggregate of all the rotations done
 *      and calculate the current paths using the initial path and the rotation
 *
 * @author BLI
 */
public class PathOnMeshCalculator {
    
    /**
     * The maximum number of rotation attempts done before 
     *      quitting and settling with the results
     */
    public static final int MAX_ROTATION_ATTEMPTS = 100;
    
    /**
     * The Epsilon we are using for this algorithm
     *      to determine that the distances have converged. 
     */
    public static final float DIFF_FOR_CONVERGENCE = 0.0001f;
    
    /*
     * This is meant to be path that is to be rotated
     *      It must be scaled and moved beforehand
     *      so that it's start point matches the desired
     *      start point.
     */
    private ArrayList<Vector3f> initPath;
    
    /*
     * This is the aggregate of all the rotations done. 
     */
    private Quaternion aggregateRotation;
    
    private Vector3f startingPoint;
    private Vector3f initTriangleNormal;
    private MeshTriangle startingTriangle;
    private TriangleSet meshInfo;
    private Vector3f endPoint;
    
    /**
     * Initializes the object which will calculate the 
     *      best rotation of the path onto the mesh
     * @param initPath              initial path
     * @param endPoint              target end point
     * @param startingTriangle      initial triangle on mesh
     * @param meshInfo              mesh to track on
     */
    public PathOnMeshCalculator(ArrayList<Vector3f> initPath, Vector3f endPoint, MeshTriangle startingTriangle, TriangleSet meshInfo){
        aggregateRotation = new Quaternion();
        this.initPath = initPath;
        startingPoint = PathHelper.getFirstPoint(initPath);
        initTriangleNormal = startingTriangle.getNormal();
        this.startingTriangle = startingTriangle;
        this.endPoint = endPoint;
        this.meshInfo = meshInfo;
        
        performRotationOntoInitialPlane();
        approximatePathLocationOnMesh();
    }

    /**
     * this does the initial transformation onto the plane of the first triangle
     */
    private void performRotationOntoInitialPlane(){

        Vector3f initEndPoint = PathHelper.getSecondPoint(initPath);
        preMultiplyNewRotation(
                RotationTransformHelper.getRotationOntoPlane(
                initTriangleNormal, startingPoint, initEndPoint));
    }
    
    /**
     * This gets the total rotation done on the initial path
     * @return  quaternion for rotation
     */
    public Quaternion getAggregateRotation(){
        return aggregateRotation;
    }
    
    /**
     * This does (rotation)*(currentRotation) and uses the value
     *      as the current rotation, where the multipliation
     *      is the equivalent of matrix multiplication of the 
     *      rotation matrices. 
     * @param rotation      quaternion for new rotation
     */
    private void preMultiplyNewRotation(Quaternion rotation){
        aggregateRotation = rotation.mult(aggregateRotation);
    }
    
    /**
     * This gets the initial path rotated using the current
     *      aggregate rotation and the origin point of rotation
     *      as the origin point of the path
     * @return      path vertices after being rotated by current aggregate rotation
     */
    public ArrayList<Vector3f> getCurrentRotatedPath(){
        return PathTransformHelper.getTransformedVertices(
                PathHelper.getCopyOfPath(initPath), 
                RotationTransformHelper.getRotationAroundPoint(startingPoint, aggregateRotation));
    }
    
    /**
     * This takes the current path rotated onto the mesh and it projects
     *      it onto the mesh and returns those vertices
     * @return      current rotated path projected onto mesh
     */
    public ArrayList<Vector3f> getCurrentPathOnSurface(){
        return PathProjectionOntoMesh.getPathProjectedOntoMesh(
                getCurrentRotatedPath(), startingTriangle, meshInfo);
    }
    
    /**
     * This takes the inputted rotation angle and find the quaternion
     *      that is the rotation described by that angle and the 
     *      initial normal as the axis
     * @param currentRotationAngle      rotation angle
     * @return                          rotation using inputted angle and triangle normal as axis
     */
    private Quaternion obtainTransformFromAngle(float currentRotationAngle){
        AngleAxisRotation currentRotationAngAxis = 
                new AngleAxisRotation(initTriangleNormal, currentRotationAngle);
        return currentRotationAngAxis.getQuat();
    }
    
    /**
     * This takes a rotation consisting of angle and axis and find the angle
     *      in it, making sure to negate it if the axis and the normal
     *      are in opposite directions
     * @param rotation      the rotation
     * @param normal        the normal to check
     * @return              the rotation angle from the input rotation, negated if necessary
     */
    public static float getRotationAngleAlongPlane(Quaternion rotation,Vector3f normal){
        //converts rotation to angle,axis form
        AngleAxisRotation rotToEndptAngAxis = new AngleAxisRotation(rotation);
        
        //finds the angle and axis
        float currentRotationAngle = rotToEndptAngAxis.getAngle();
        Vector3f rotToEndptAxis = rotToEndptAngAxis.getAxis();
        
        /*
         * If the axis and the normal point in opposite directions,
         *      then you want to negate the angle to make it look correct
         */
        if (normal.dot(rotToEndptAxis) < 0) {
            currentRotationAngle = -1 * currentRotationAngle;
        }
        
        return currentRotationAngle;
    }
    
    /**
     * This gets the rotation angle along the surface to use
     * @param currentPath       current path we care about
     * @return                  rotation to get end points to match
     */
    private float getRotationAngleAlongSurface(ArrayList<Vector3f> currentPath){
        //finds the rotation angle
        Quaternion rotationToEndpoint = PathTransformHelper.getTransformOfEndpoint(currentPath, endPoint);
        return getRotationAngleAlongPlane(rotationToEndpoint,initTriangleNormal);
    }

    /**
     * This executes the algorithm described above that approximates
     *      the path location on the mesh
     */
    private void approximatePathLocationOnMesh() {

        //this does NOT follow the surface yet
        //  hence why I am calling the rotated path getter
        ArrayList<Vector3f> currentPathOnSurface = getCurrentRotatedPath();

        float currentRotationAngle;
        float currentDistance=0,previousDistance = 0;
        
        for (int tryNum = 1; tryNum <= MAX_ROTATION_ATTEMPTS; tryNum++) {
            
            //finds the rotation angle
            currentRotationAngle = getRotationAngleAlongSurface(currentPathOnSurface);
            
            //gets the transform
            preMultiplyNewRotation(obtainTransformFromAngle(currentRotationAngle));
            
            //projects the rotated path on the surface
            currentPathOnSurface = getCurrentPathOnSurface();
            
            //sees how close we are to matching endpoints
            currentDistance = PathHelper.getCurrentEndpointDistance(currentPathOnSurface, endPoint);

            //uses Cauchy convergence to stop when the distance has converged
            if (MathHelper.hasSequenceConverged(previousDistance,currentDistance, PathOnMeshCalculator.DIFF_FOR_CONVERGENCE)) {
                break;
            }
            
            previousDistance = currentDistance;
        }
        
        //displays the results
        displayDistResult(currentDistance);
        displayQuatResult();
    }
    
    
    /**
     * Displays the aggegate rotation quaternion
     */
    private void displayQuatResult(){
        System.out.println("Rotation Quaternion: " + aggregateRotation);
    }
    
    /**
     * Displays the distance result
     * @param currentDistance   distance value
     */
    private void displayDistResult(float currentDistance){
        System.out.println("Distance from target endpoint to actual endpoint: " + currentDistance);
        System.out.println("Final Rotation was: " + aggregateRotation);
    }
    
    /*
     * * ********IMPORTANT NOTE*****************
     * UNUSED CODE BELOW HERE
     * PLEASE KEEP THOUGH FOR POTENTIAL
     *      FUTURE USES
     * ***************************************
     *
     * 
     * 
    public void rotateAndProjectCurrentPath(Vector3f endPoint, MeshTriangle startingTriangle, TriangleSet meshInfo, AssetManager assetManager){
        scaleCurrentPathEndpoint(endPoint);
        compressCurrentPath();
        transformCurrentPathEndpoint(endPoint);
        
        ArrayList<Vector3f> currentRotatedPath;
        ArrayList<Vector3f> currentPathOnSurface = new ArrayList<Vector3f>();
        
        float numberTries = 10;
        float currentDistance;
        
        Matrix4f rotationToEndpoint;
        currentRotatedPath = getCurrentPath().getVertices();

        for(float tryNum = 0; tryNum <= numberTries; tryNum++){
            //rotatation of current path to endpoint
            rotationToEndpoint = PathTransformHelper.getTransformOfEndpoint(getCurrentPath().getVertices(),endPoint);
            
            //find the rotated path
            currentRotatedPath = MeshTraverseHelper.getTransformedVertices(
                    currentRotatedPath, 
                    rotationToEndpoint);
            
            //projects the rotated path on the surface
            currentPathOnSurface = PathProjectionOntoMesh.findPathProjectionOntoMesh(
                    currentRotatedPath,startingTriangle,meshInfo);
            
            currentDistance = getCurrentEndpointDistance(currentPathOnSurface,endPoint);
            System.out.println("Distance from target endpoint to actual endpoint: " + currentDistance);
            
            if(currentDistance < 0.1){
                break;
            }
            
            //saves the path for later display
            //addPathToSaveList(currentPathOnSurface,getGrayscaleMaterial(tryNum/numberTries,assetManager));
        }
        addPath(currentPathOnSurface);
    }
    */
    
    
}
