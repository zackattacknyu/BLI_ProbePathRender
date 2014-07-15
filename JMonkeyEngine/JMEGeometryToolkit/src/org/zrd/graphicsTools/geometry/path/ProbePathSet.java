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
    
    public boolean importPathUsingFileSelector(File initialImportDirectory){
        File selectedFile = FileDataHelper.importPathUsingFileSelector(initialImportDirectory);
        if(selectedFile == null){
            return false;
        }else{
            addPath(ProbeDataHelper.getVerticesFromFile(selectedFile));
            return true;
        }
    }

    
}
