/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;

/**
 *
 * @author BLI
 */
public class ProbePathSet {
    
    private ArrayList<ProbePath> paths;
    private int currentIndex = 0;
    private ProbePath currentPath;
    private Material lineMaterial;
    
    public ProbePathSet(Material lineMaterial){
        paths = new ArrayList<ProbePath>();
        this.lineMaterial = lineMaterial;
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
    
    public void transformCurrentPathEndpoint(Vector3f newEndpoint){
        addPath(currentPath.transformEndpoint(newEndpoint));
        addPath(currentPath.scaleForNewEndpoint(newEndpoint));
    }
    
    public boolean importPathUsingFileSelector(File initialImportDirectory){
        JFileChooser selector = new JFileChooser(initialImportDirectory);
        int chosenOption = selector.showOpenDialog(null);
        boolean optionChosen = false;
        if(chosenOption == JFileChooser.APPROVE_OPTION){
            optionChosen = true;
            File selectedFile = selector.getSelectedFile();
            addPath(ProbeDataHelper.getVerticesFromFile(selectedFile));
        }
        return optionChosen;
    }
}
