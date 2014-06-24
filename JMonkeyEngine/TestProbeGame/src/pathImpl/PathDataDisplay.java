/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pathImpl;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.io.File;
import java.util.ArrayList;
import probeTracking.ProbeDataHelper;

/**
 *
 * @author Zach
 */
public abstract class PathDataDisplay {

    protected ArrayList<Vector3f> displayVertices;
    
    protected boolean nullReturn = false;
    
    protected PathDataDisplay(File initDir){
        File startingFile = ProbeDataHelper.importPathUsingFileSelector(initDir);
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
        return PathHelper.createLineFromVertices(displayVertices, mat);
    }
    
    protected void importFromFile(File selectedFile){
        ArrayList<String> lines = ProbeDataHelper.getLinesFromFile(selectedFile);
        for(String line: lines){
            String[] parts = line.split(",");
            addParts(parts);
        }
    }
    
    protected abstract void addParts(String[] parts);
    
    protected abstract void generateDisplayValues();
    
}
