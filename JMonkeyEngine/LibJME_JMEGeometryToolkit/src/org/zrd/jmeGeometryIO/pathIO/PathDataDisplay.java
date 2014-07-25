/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryIO.pathIO;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.io.File;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometryUtil.ProbeDataHelper;
import org.zrd.util.fileHelper.FileDataHelper;

/**
 *
 * @author Zach
 */
public abstract class PathDataDisplay {

    protected ArrayList<Vector3f> displayVertices;
    
    protected boolean nullReturn = false;
    
    protected PathDataDisplay(File initDir){
        File startingFile = FileDataHelper.importPathUsingFileSelector(initDir);
        if(startingFile == null){
            nullReturn = true;
        }else{
            initializeArrayLists();
            importFromFile(startingFile);
            generateDisplayValues();
        }
    }
    
    protected abstract void initializeArrayLists();

    public boolean isNullReturn() {
        return nullReturn;
    }
    
    public Spatial generateSpatial(Material mat){
        return PathRenderHelper.createLineFromVertices(displayVertices, mat);
    }
    
    protected void importFromFile(File selectedFile){
        ArrayList<String> lines = FileDataHelper.getLinesFromFile(selectedFile);
        for(String line: lines){
            String[] parts = line.split(",");
            addParts(parts);
        }
    }
    
    protected abstract void addParts(String[] parts);
    
    protected abstract void generateDisplayValues();
    
    public abstract Spatial generateReferenceObject(Material mat);
    
}
