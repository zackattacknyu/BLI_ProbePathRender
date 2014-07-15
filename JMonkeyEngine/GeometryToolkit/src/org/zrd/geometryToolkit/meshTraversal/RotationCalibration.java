/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.meshTraversal;

import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometryUtil.AngleAxisRotation;
import org.zrd.geometryToolkit.geometryUtil.GeneralHelper;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.pathTools.PathTransformHelper;

/**
 * This houses the code that takes in a mesh and a path
 *      as well as the target start and end points
 *      on the mesh and tries to make the path follow 
 *      the mesh so that the end points match
 *
 * @author BLI
 */
public class RotationCalibration {
    
    public static final int MAX_ROTATION_ATTEMPTS = 100;
    public static final float DIFF_FOR_CONVERGENCE = 0.0001f;
    
    /*
     * This is meant to be path that is to be rotated
     *      It must be scaled and moved beforehand
     *      so that it's start point matches the desired
     *      start point.
     */
    private ArrayList<Vector3f> initPath;
    
    /*
     * This is the aggregate transformation to be done on the vertices of the
     *      original path before the path is projected onto the plane
     */
    private Matrix4f aggregateTransform;
    
    private Vector3f initTriangleNormal;
    private MeshTriangle startingTriangle;
    private TriangleSet meshInfo;
    private Vector3f endPoint;
    
    public RotationCalibration(ArrayList<Vector3f> initPath, Vector3f endPoint, MeshTriangle startingTriangle, TriangleSet meshInfo){
        aggregateTransform = new Matrix4f();
        this.initPath = initPath;
        initTriangleNormal = startingTriangle.getNormal();
        this.startingTriangle = startingTriangle;
        this.endPoint = endPoint;
        this.meshInfo = meshInfo;
        
        performRotationCalibration();
    }
    
    private void performRotationOntoInitialPlane(){
        //this does the initial transformation onto the plane of the first triangle
        Vector3f initPoint = initPath.get(0);
        Vector3f initEndPoint = initPath.get(1);
        Matrix4f initTransform = MeshTraverseHelper.getRotationOntoPlane(
                initTriangleNormal, initPoint, initEndPoint);
        postMultiplyNewTransform(initTransform);
    }
    
    public Quaternion getAggregateRotation(){
        return aggregateTransform.toRotationQuat();
    }
    
    private void postMultiplyNewTransform(Matrix4f transform){
        aggregateTransform = transform.mult(aggregateTransform);
    }
    
    public ArrayList<Vector3f> getCurrentRotatedPath(){
        return MeshTraverseHelper.getTransformedVertices(
                GeneralHelper.getCopyOfPath(initPath), aggregateTransform);
    }
    
    public ArrayList<Vector3f> getCurrentPathOnSurface(){
        return PathProjectionOntoMesh.getPathProjectedOntoMesh(
                getCurrentRotatedPath(), startingTriangle, meshInfo);
    }

    private void performRotationCalibration() {
        
        performRotationOntoInitialPlane();
        
        //this does NOT follow the surface yet
        ArrayList<Vector3f> currentPathOnSurface = getCurrentRotatedPath();

        Matrix4f currentRotationTransform;
        float currentRotationAngle;

        float currentDistance,previousDistance = 0;
        
        for (int tryNum = 1; tryNum <= MAX_ROTATION_ATTEMPTS; tryNum++) {
            
            //finds the rotation angle
            currentRotationAngle = getRotationAngleAlongSurface(currentPathOnSurface);
            
            //gets the transform
            currentRotationTransform = obtainTransformFromAngle(currentRotationAngle);
            postMultiplyNewTransform(currentRotationTransform);
            
            //projects the rotated path on the surface
            currentPathOnSurface = getCurrentPathOnSurface();
            
            //sees how close we are to matching endpoints
            currentDistance = currentEndpointDistance(currentPathOnSurface, endPoint);
            
            //displays the results
            displayAttemptResults(tryNum,currentDistance);
            
            //uses Cauchy convergence to stop when the distance has converged
            if (hasConverged(previousDistance,currentDistance)) {
                break;
            }
            
            previousDistance = currentDistance;
        }
    }
    
    private boolean hasConverged(float currentDistance, float previousDistance){
        return (Math.abs(currentDistance-previousDistance) < DIFF_FOR_CONVERGENCE);
    }
    
    private void displayAttemptResults(int tryNum, float currentDistance){
        System.out.println("After Attempt Number: " + tryNum);
        System.out.println("Distance from target endpoint to actual endpoint: " + currentDistance);
    }
    
    private Matrix4f obtainTransformFromAngle(float currentRotationAngle){
        AngleAxisRotation currentRotationAngAxis = 
                new AngleAxisRotation(initTriangleNormal, currentRotationAngle);
        return MeshTraverseHelper.getRotationAroundPoint(
                initPath.get(0), currentRotationAngAxis.getQuat());
    }
    
    public static float findRotationAngleAlongPlane(Matrix4f transform,Vector3f normal){
        AngleAxisRotation rotToEndptAngAxis = new AngleAxisRotation(transform.toRotationQuat());
        float currentRotationAngle = rotToEndptAngAxis.getAngle();
        Vector3f rotToEndptAxis = rotToEndptAngAxis.getAxis();
        if (normal.dot(rotToEndptAxis) < 0) {
            currentRotationAngle = -1 * currentRotationAngle;
        }
        return currentRotationAngle;
    }
    
    private float getRotationAngleAlongSurface(ArrayList<Vector3f> currentPath){
        //finds the rotation angle
        Matrix4f rotationToEndpoint = PathTransformHelper.getTransformOfEndpoint(currentPath, endPoint);
        return findRotationAngleAlongPlane(rotationToEndpoint,initTriangleNormal);
    }
    
    public static float currentEndpointDistance(ArrayList<Vector3f> path, Vector3f targetEndpoint){
        Vector3f actualEndpoint = path.get(path.size()-1);
        return actualEndpoint.clone().distance(targetEndpoint.clone());
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
            
            currentDistance = currentEndpointDistance(currentPathOnSurface,endPoint);
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
