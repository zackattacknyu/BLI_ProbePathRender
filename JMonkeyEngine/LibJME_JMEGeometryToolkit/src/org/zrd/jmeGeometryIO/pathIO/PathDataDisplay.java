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
import org.zrd.util.fileHelper.FileDataHelper;
import org.zrd.util.fileHelper.GeneralFileHelper;

/**
 * 
 * These are used to generate various data displays
 *      for debugging purposes. It displays the data
 *      as well as a reference object to make
 *      sure the data makes sense. 
 *
 * @author Zach
 */
public abstract class PathDataDisplay {

    protected ArrayList<Vector3f> displayVertices;
    
    protected boolean nullReturn = false;
    
    /**
     * This takes in an initial directory and directs the user
     *      to select a file to use. It then calls the methods
     *      that render the data in those files. 
     * @param initDir 
     */
    protected PathDataDisplay(File initDir){
        File startingFile = GeneralFileHelper.importPathUsingFileSelector(initDir);
        if(startingFile == null){
            nullReturn = true;
        }else{
            initializeArrayLists();
            importFromFile(startingFile);
            generateDisplayValues();
        }
    }
    
    protected abstract void initializeArrayLists();

    /**
     * Whether or not a file was selected
     * @return  if file was selected
     */
    public boolean isNullReturn() {
        return nullReturn;
    }
    
    /**
     * Takes the display vertices and renders them
     * @param mat       material to use for rendering
     * @return          rendering of display vertices
     */
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
    
    /**
     * Generates the reference object that helps verify
     *      that the data makes sense
     * @param mat       material to overlay on reference object
     * @return          reference object rendering
     */
    public abstract Spatial generateReferenceObject(Material mat);
    
}
