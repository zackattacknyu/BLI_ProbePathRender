/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryIO.renderedObjects;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 *
 * This generates and transforms the background box that is meant
 *      to be put behind the Lola Mesh when using that one in a JME application
 * 
 * @author BLI
 */
public class BackgroundBox {
    
    private Spatial background;
    
    public BackgroundBox(AssetManager assetManager){
        
        Material backgroundBoxMaterial = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        backgroundBoxMaterial.setTexture("ColorMap",assetManager.loadTexture("Textures/table_texture.jpg"));
        
        background = initBackgroundBox(backgroundBoxMaterial, "background");
    }
    
    public static Spatial getBackgroundBox(AssetManager assetManager){
        BackgroundBox back = new BackgroundBox(assetManager);
        return back.background;
    }
    
    private Spatial initBackgroundBox(Material ballMat, String name){
        Box b = new Box(30f, 30f, 2f);
        Spatial sampleBox = new Geometry("Background", b);
        sampleBox.setCullHint(Spatial.CullHint.Never);
        sampleBox.setName(name);
        sampleBox.setLocalScale(1);
        sampleBox.setMaterial(ballMat);
        sampleBox.setLocalTranslation(10.0f, -10.0f, 20.0f);
        return sampleBox;
    }
    
}
