/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.graphicsTools.geometry.path;

import org.zrd.geometryToolkit.pathTools.PathTransformHelper;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix4f;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.io.File;
import java.util.ArrayList;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.meshTraversal.MeshFollowHelper;
import org.zrd.geometryToolkit.meshTraversal.MeshTraverseHelper;
import org.zrd.geometryToolkit.geometryUtil.AngleAxisRotation;
import org.zrd.geometryToolkit.geometryUtil.ProbeDataHelper;
import org.zrd.geometryToolkit.geometryUtil.ProgramConstants;
import org.zrd.geometryToolkit.pathTools.PathCompression;
import org.zrd.util.fileHelper.FileDataHelper;

/**
 *
 * @author BLI
 */
public class ProbePathSet {
    
    private ArrayList<ProbePath> paths;
    private int currentIndex = 0;
    private ProbePath currentPath;
    private Material lineMaterial;
    private ArrayList<ProbePath> pathsToSave;
    
    public ProbePathSet(Material lineMaterial){
        paths = new ArrayList<ProbePath>();
        pathsToSave = new ArrayList<ProbePath>();
        this.lineMaterial = lineMaterial;
    }

    public ArrayList<ProbePath> getPathsToSave() {
        return pathsToSave;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public ProbePath getCurrentPath() {
        return currentPath;
    }
    
    public Spatial getCurrentPathSpatial(){
        return currentPath.getProbePath();
    }
    
    public void addPath(ArrayList<Vector3f> vertices){
        addPath(vertices,lineMaterial);
    }
    
    public void addPath(ArrayList<Vector3f> vertices, Material mat){
        currentPath = new ProbePath(vertices,mat);
        paths.add(currentPath);
        currentIndex++;
    }
    
    public void addPathToSaveList(ArrayList<Vector3f> vertices, Material mat){
        addPath(vertices,mat);
        pathsToSave.add(currentPath);
    }
    
    public void transformCurrentPathEndpoint(Vector3f newEndpoint, Material mat){
        addPath(PathTransformHelper.transformPathEndpoint(
                currentPath.getVertices(), newEndpoint),mat);
    }
    
    public void scaleCurrentPathEndpoint(Vector3f newEndpoint, Material mat){
        addPath(PathTransformHelper.scalePathForNewEndpoint(
                currentPath.getVertices(), newEndpoint),mat);
    }
    
    public void transformCurrentPathEndpoint(Vector3f newEndpoint){
       transformCurrentPathEndpoint(newEndpoint,lineMaterial);
    }
    
    public void scaleCurrentPathEndpoint(Vector3f newEndpoint){
        scaleCurrentPathEndpoint(newEndpoint,lineMaterial);
    }
    
    public void compressCurrentPath(){
        addPath(PathCompression.getCompressedPath(
                currentPath.getVertices(), 
                ProgramConstants.MIN_SEGMENT_LENGTH));
    }
    
    public boolean importPathUsingFileSelector(File initialImportDirectory){
        File selectedFile = FileDataHelper.importPathUsingFileSelector(initialImportDirectory);
        if(selectedFile == null){
            return false;
        }else{
            addPath(ProbeDataHelper.getVerticesFromFile(selectedFile));
            return true;
        }
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
            currentPathOnSurface = MeshFollowHelper.makePathFollowMesh2(
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
    
    public static float currentEndpointDistance(ArrayList<Vector3f> path, Vector3f targetEndpoint){
        Vector3f actualEndpoint = path.get(path.size()-1);
        return actualEndpoint.clone().distance(targetEndpoint.clone());
    }
    
    private Material getGrayscaleMaterial(float brightness, AssetManager assetManager){
        Material outputMaterial = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        ColorRGBA grayColor = new ColorRGBA(brightness,brightness,brightness,1.0f);
        outputMaterial.setColor("Color", grayColor);
        return outputMaterial;
    }
    
    
    public void rotateAndProjectCurrentPath2(Vector3f endPoint, MeshTriangle startingTriangle, TriangleSet meshInfo, AssetManager assetManager){
        scaleCurrentPathEndpoint(endPoint);
        compressCurrentPath();
        //displayCurrentPath();
        ArrayList<Vector3f> initScaledPath = getCurrentPath().getVertices();
        ArrayList<Vector3f> initProjectedPath = MeshFollowHelper.projectPathOntoPlane(initScaledPath, startingTriangle.getNormal());
        addPath(initProjectedPath);
        Vector3f rotationAxis = startingTriangle.getNormal();

        Matrix4f rotationToEndpoint,currentRotationTransform;
        float currentRotationAngle;
        AngleAxisRotation currentRotationAngAxis;
        ArrayList<Vector3f> currentRotatedPath,currentPathOnSurface;
        Vector3f rotToEndptAxis;
        float totalAngle = 0;
        float currentDistance;
        
        float numberTries = 4;
        
        currentRotatedPath = getCurrentPath().getVertices();
        currentPathOnSurface = getCurrentPath().getVertices();

        for(float tryNum = 0; tryNum <= numberTries; tryNum++){
            //rotatation of current path to endpoint 
            rotationToEndpoint = PathTransformHelper.getTransformOfEndpoint(currentPathOnSurface, endPoint);
            AngleAxisRotation rotToEndptAngAxis = 
                    new AngleAxisRotation(rotationToEndpoint.toRotationQuat());
            currentRotationAngle = rotToEndptAngAxis.getAngle();
            rotToEndptAxis = rotToEndptAngAxis.getAxis();

            //in case they point in opposite directions, does the negation of angle
            if(rotationAxis.dot(rotToEndptAxis) < 0){
                //the axes normals could be flipped
                currentRotationAngle = -1*currentRotationAngle;
            }
            totalAngle += currentRotationAngle;
            
            //DEBUG code
            System.out.println("Try: " + tryNum + ", Angle: " + 
                    currentRotationAngle + ", TotalAngle: " + totalAngle);
            
            //gets the rotation we will actually do which is on the surface
            //      of the starting Triangle
            currentRotationAngAxis = 
                    new AngleAxisRotation(rotationAxis,currentRotationAngle);
            currentRotationTransform = MeshTraverseHelper.getRotationAroundPoint(
                    getCurrentPath().getVertices().get(0), 
                    currentRotationAngAxis.getQuat());
            
            //find the rotated path
            currentRotatedPath = MeshTraverseHelper.getTransformedVertices(
                    currentRotatedPath, 
                    currentRotationTransform);
            
            //projects the rotated path on the surface
            currentPathOnSurface = MeshFollowHelper.makePathFollowMesh2(
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
}
