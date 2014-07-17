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
import org.zrd.graphicsToolsImpl.pathImplDebug.PathYawPitchRollDataDisplay;
import org.zrd.jmeUtil.materials.MaterialHelper;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;

/**
 *
 * @author BLI
 */
public class RawYawPitchRollDataImport extends GeneralKeyboardActionMethod{
    
    private Node rootNode;
    private File initialImportDirectory;
    private Material redMaterial;
    private Material greenMaterial;

    public RawYawPitchRollDataImport(InputManager inputManager, AssetManager assetManager, Node rootNode, File initialImportDirectory){
        super(inputManager,"rawYawPitchRollDisplay",KeyInput.KEY_G);
        this.rootNode = rootNode;
        this.initialImportDirectory = initialImportDirectory;
        
        redMaterial = MaterialHelper.makeColorMaterial(assetManager,ColorRGBA.Red);
        greenMaterial = MaterialHelper.makeColorMaterial(assetManager,ColorRGBA.Green);
    }
    
    @Override
    public void actionMethod() {
        PathYawPitchRollDataDisplay probeData = PathYawPitchRollDataDisplay.obtainYawPitchRollProbeData(initialImportDirectory);
        rootNode.attachChild(probeData.generateSpatial(redMaterial));
        rootNode.attachChild(probeData.generateReferenceObject(greenMaterial));
    }
    
}
