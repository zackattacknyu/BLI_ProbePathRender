/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.rawProbeDataDisplay.rawDataRendering;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import java.io.File;
import org.zrd.jmeGeometry.pathRendering.PathXYDataDisplay;
import org.zrd.jmeUtil.materials.MaterialHelper;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;

/**
 *
 * @author BLI
 */
public class RawXYDataImport extends GeneralKeyboardActionMethod{
    
    private Node rootNode;
    private File initialImportDirectory;
    private Material orangeMaterial;
    private Material greenMaterial;

    public RawXYDataImport(InputManager inputManager, AssetManager assetManager, Node rootNode, File initialImportDirectory){
        super(inputManager,"rawXYDataDisplay",KeyInput.KEY_T);
        this.rootNode = rootNode;
        this.initialImportDirectory = initialImportDirectory;
        
        orangeMaterial = MaterialHelper.getColorMaterial(assetManager,ColorRGBA.Orange);
        greenMaterial = MaterialHelper.getColorMaterial(assetManager,ColorRGBA.Green);
    }
    
    @Override
    public void actionMethod() {
        PathXYDataDisplay probeData = PathXYDataDisplay.obtainXYProbeData(initialImportDirectory);
        rootNode.attachChild(probeData.generateSpatial(orangeMaterial));
        rootNode.attachChild(probeData.generateReferenceObject(greenMaterial));
    }
    
}
