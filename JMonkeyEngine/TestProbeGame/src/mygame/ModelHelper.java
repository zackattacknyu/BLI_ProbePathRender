/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Spatial;

/**
 *
 * @author Zach
 */
public class ModelHelper {
    
    public static Spatial generateModel(String objFileLocation, Material ballMat, AssetManager assetManager){
        Spatial sampleMesh = assetManager.loadModel(objFileLocation);
        sampleMesh.setMaterial(ballMat); 
        sampleMesh.scale(40f);
        return sampleMesh;
    }
}
