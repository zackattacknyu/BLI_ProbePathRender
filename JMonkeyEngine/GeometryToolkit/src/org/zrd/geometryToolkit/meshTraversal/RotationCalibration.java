/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.meshTraversal;

import com.jme3.math.Matrix4f;
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

    public static ArrayList<Vector3f> performRotationCalibration(ArrayList<Vector3f> initPath, Vector3f endPoint, MeshTriangle startingTriangle, TriangleSet meshInfo) {
        Vector3f initPoint = initPath.get(0);
        Vector3f initEndPoint = initPath.get(1);
        Matrix4f aggregateTransform = new Matrix4f();
        Matrix4f currentTransform = MeshTraverseHelper.getRotationOntoPlane(startingTriangle.getNormal(), initPoint, initEndPoint);
        aggregateTransform = currentTransform.mult(aggregateTransform);
        ArrayList<Vector3f> currentRotatedPath = MeshTraverseHelper.getTransformedVertices(initPath, aggregateTransform);
        Vector3f rotationAxis = startingTriangle.getNormal();
        Matrix4f rotationToEndpoint;
        Matrix4f currentRotationTransform;
        float currentRotationAngle;
        AngleAxisRotation currentRotationAngAxis;
        ArrayList<Vector3f> currentPathOnSurface = GeneralHelper.getCopyOfPath(currentRotatedPath);
        Vector3f rotToEndptAxis;
        float totalAngle = 0;
        float currentDistance;
        float numberTries = 10;
        for (float tryNum = 0; tryNum <= numberTries; tryNum++) {
            rotationToEndpoint = PathTransformHelper.getTransformOfEndpoint(currentPathOnSurface, endPoint);
            AngleAxisRotation rotToEndptAngAxis = new AngleAxisRotation(rotationToEndpoint.toRotationQuat());
            currentRotationAngle = rotToEndptAngAxis.getAngle();
            rotToEndptAxis = rotToEndptAngAxis.getAxis();
            if (rotationAxis.dot(rotToEndptAxis) < 0) {
                currentRotationAngle = -1 * currentRotationAngle;
            }
            totalAngle += currentRotationAngle;
            System.out.println("Try: " + tryNum + ", Angle: " + currentRotationAngle + ", TotalAngle: " + totalAngle);
            currentRotationAngAxis = new AngleAxisRotation(rotationAxis, currentRotationAngle);
            currentRotationTransform = MeshTraverseHelper.getRotationAroundPoint(currentPathOnSurface.get(0), currentRotationAngAxis.getQuat());
            aggregateTransform = currentRotationTransform.mult(aggregateTransform);
            currentRotatedPath = MeshTraverseHelper.getTransformedVertices(initPath, aggregateTransform);
            currentPathOnSurface = PathProjectionOntoMesh.findPathProjectionOntoMesh(currentRotatedPath, startingTriangle, meshInfo);
            currentDistance = currentEndpointDistance(currentPathOnSurface, endPoint);
            System.out.println("After Attempt Number: " + (tryNum + 1));
            System.out.println("Distance from target endpoint to actual endpoint: " + currentDistance);
            if (currentDistance < 0.1) {
                break;
            }
        }
        return currentPathOnSurface;
    }
    
    public static float currentEndpointDistance(ArrayList<Vector3f> path, Vector3f targetEndpoint){
        Vector3f actualEndpoint = path.get(path.size()-1);
        return actualEndpoint.clone().distance(targetEndpoint.clone());
    }
    
    //first path inputted
    private ArrayList<Vector3f> originalPath;
    
    //path moved so that its start point is the same as target start point
    private ArrayList<Vector3f> initiallyMovedPath;
    
    
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
}
