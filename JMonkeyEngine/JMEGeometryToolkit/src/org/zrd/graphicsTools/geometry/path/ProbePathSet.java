/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.graphicsTools.geometry.path;

import org.zrd.geometryToolkit.pathTools.PathTransformHelper;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.io.File;
import java.util.ArrayList;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.geometryUtil.ProbeDataHelper;
import org.zrd.geometryToolkit.geometryUtil.ProgramConstants;
import org.zrd.geometryToolkit.meshTraversal.RotationCalibration;
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
    
    
    
    
    private Material getGrayscaleMaterial(float brightness, AssetManager assetManager){
        Material outputMaterial = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        ColorRGBA grayColor = new ColorRGBA(brightness,brightness,brightness,1.0f);
        outputMaterial.setColor("Color", grayColor);
        return outputMaterial;
    }
    
    public void rotateAndProjectCurrentPath(Vector3f endPoint, MeshTriangle startingTriangle, TriangleSet meshInfo){
        RotationCalibration newCalibration = new RotationCalibration(
                getCurrentPath().getVertices(),endPoint,startingTriangle,meshInfo);
        addPath(newCalibration.getFinalPathOnSurface());
    }
}
