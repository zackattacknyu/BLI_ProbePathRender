/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeUtil.materials;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;

/**
 *
 * @author Zach
 */
public class MaterialHelper {

    public static Material makeColorMaterial(AssetManager assetManager, ColorRGBA color) {
        Material returnMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        returnMat.setColor("Color", color);
        return returnMat;
    }
    
    public static Material getColorMaterial(float red, float green, float blue, AssetManager assetManager){
        ColorRGBA color = new ColorRGBA(red,green,blue,1.0f);
        return makeColorMaterial(assetManager,color);
    }

    public static Material getGrayscaleMaterial(float brightness, AssetManager assetManager) {
        Material outputMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        ColorRGBA grayColor = new ColorRGBA(brightness, brightness, brightness, 1.0F);
        outputMaterial.setColor("Color", grayColor);
        return outputMaterial;
    }
    
}
