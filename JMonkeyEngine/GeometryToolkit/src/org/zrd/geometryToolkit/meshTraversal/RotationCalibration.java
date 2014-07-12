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
    private Quaternion aggregateRotation;
    
    private ArrayList<Vector3f> finalRotatedPath;
    private ArrayList<Vector3f> finalPathOnSurface;
    
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
        aggregateTransform = initTransform.mult(aggregateTransform);
    }

    private void performRotationCalibration() {
        
        performRotationOntoInitialPlane();
        
        ArrayList<Vector3f> currentRotatedPath = 
                MeshTraverseHelper.getTransformedVertices(initPath, aggregateTransform);
        ArrayList<Vector3f> currentPathOnSurface = 
                GeneralHelper.getCopyOfPath(currentRotatedPath);
        Matrix4f rotationToEndpoint;
        Matrix4f currentRotationTransform;
        float currentRotationAngle;
        AngleAxisRotation currentRotationAngAxis;
        
        Vector3f rotToEndptAxis;
        float currentDistance;
        float numberTries = 10;
        
        for (float tryNum = 0; tryNum <= numberTries; tryNum++) {
            //finds the rotation angle
            rotationToEndpoint = PathTransformHelper.getTransformOfEndpoint(currentPathOnSurface, endPoint);
            AngleAxisRotation rotToEndptAngAxis = new AngleAxisRotation(rotationToEndpoint.toRotationQuat());
            currentRotationAngle = rotToEndptAngAxis.getAngle();
            rotToEndptAxis = rotToEndptAngAxis.getAxis();
            if (initTriangleNormal.dot(rotToEndptAxis) < 0) {
                currentRotationAngle = -1 * currentRotationAngle;
            }
            
            //does the rotation using the desired axis but with the angle found above
            currentRotationAngAxis = new AngleAxisRotation(initTriangleNormal, currentRotationAngle);
            currentRotationTransform = MeshTraverseHelper.getRotationAroundPoint(currentPathOnSurface.get(0), currentRotationAngAxis.getQuat());
            aggregateTransform = currentRotationTransform.mult(aggregateTransform);
            currentRotatedPath = MeshTraverseHelper.getTransformedVertices(initPath, aggregateTransform);
            
            //projects the rotated path on the surface
            currentPathOnSurface = PathProjectionOntoMesh.findPathProjectionOntoMesh(currentRotatedPath, startingTriangle, meshInfo);
            
            //sees how close we are to matching endpoints
            currentDistance = currentEndpointDistance(currentPathOnSurface, endPoint);
            System.out.println("After Attempt Number: " + (tryNum + 1));
            System.out.println("Distance from target endpoint to actual endpoint: " + currentDistance);
            if (currentDistance < 0.1) {
                break;
            }
        }
        finalRotatedPath = GeneralHelper.getCopyOfPath(currentRotatedPath);
        finalPathOnSurface = GeneralHelper.getCopyOfPath(currentPathOnSurface);
    }
    
    public static float currentEndpointDistance(ArrayList<Vector3f> path, Vector3f targetEndpoint){
        Vector3f actualEndpoint = path.get(path.size()-1);
        return actualEndpoint.clone().distance(targetEndpoint.clone());
    }
    
    
    
    
    /*
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

    public ArrayList<Vector3f> getFinalRotatedPath() {
        return finalRotatedPath;
    }

    public ArrayList<Vector3f> getFinalPathOnSurface() {
        return finalPathOnSurface;
    }
}
