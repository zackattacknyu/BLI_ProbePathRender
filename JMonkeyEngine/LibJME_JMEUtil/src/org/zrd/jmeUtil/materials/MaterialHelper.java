/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeUtil.materials;

import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import java.io.File;
import java.awt.image.*;
import org.zrd.util.fileHelper.ImageFileHelper;

/**
 *This has methods that help make material objects
 *      to be used when rendering geometries
 * 
 * @author Zach
 */
public class MaterialHelper {

    /**
     * This gets the material that consists of the 
     *      specified color
     * @param assetManager  the application's asset manager
     * @param color         the color to make the material
     * @return              the material object that consists of that color
     */
    public static Material getColorMaterial(AssetManager assetManager, ColorRGBA color) {
        Material returnMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        returnMat.setColor("Color", color);
        return returnMat;
    }
    
    /**
     * This gets the material that consists of the color specified
     *      by the red,green,blue values
     * @param red               red value of color
     * @param green             green value of color
     * @param blue              blue value of color
     * @param assetManager      asset manager of the application
     * @return                  material object that consists of that color
     */
    public static Material getColorMaterial(float red, float green, float blue, AssetManager assetManager){
        ColorRGBA color = new ColorRGBA(red,green,blue,1.0f);
        return getColorMaterial(assetManager,color);
    }

    /**
     * This gets the material that consists of the grayscale color
     *      specified by the brightness
     * @param brightness        brightness value of grayscale color
     * @param assetManager      application's asset manager
     * @return                  material object for that grayscale color
     */
    public static Material getGrayscaleMaterial(float brightness, AssetManager assetManager) {
        Material outputMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        ColorRGBA grayColor = new ColorRGBA(brightness, brightness, brightness, 1.0F);
        outputMaterial.setColor("Color", grayColor);
        return outputMaterial;
    }
    
    public static Material getTextureMaterial(AssetManager assetManager, BufferedImage img){
        File tempImage = ImageFileHelper.getTempImageFile();
        ImageFileHelper.writePNGimage(img, tempImage);
        Material textureMaterial = getTextureMaterial(assetManager,tempImage);
        ImageFileHelper.deleteTempImage(tempImage);
        return textureMaterial;
    }
    
    public static Material getTextureMaterial(AssetManager assetManager, String textureFileLocation){
        Material objectMaterial = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        objectMaterial.setTexture("ColorMap",assetManager.loadTexture(textureFileLocation));
        return objectMaterial;
    }
    
    public static Material getTextureMaterial(AssetManager assetManager, File textureFile){
        assetManager.registerLocator(textureFile.getParentFile().getAbsolutePath(), FileLocator.class);
        
        return getTextureMaterial(assetManager,textureFile.getName());
    }
    
}
