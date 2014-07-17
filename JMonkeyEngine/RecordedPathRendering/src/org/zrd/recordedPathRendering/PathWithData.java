/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.recordedPathRendering;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import org.zrd.graphicsToolsImpl.pathImpl.PathRenderHelper;
import org.zrd.jmeUtil.materials.MaterialHelper;

/**
 *
 * @author BLI
 */
public class PathWithData {
    
    public static Node getPathSpatialWithSampleData(ArrayList<Vector3f> pathVertices, AssetManager assetManager){
        Node outputNode = new Node();
        Spatial currentSeg;
        Material currentMaterial;
        
        ArrayList<Vector3f> currentPath = new ArrayList<Vector3f>(2);
        currentPath.add(pathVertices.get(0).clone());
        for(int index = 1; index < pathVertices.size()-1;index++){
            currentPath.add(pathVertices.get(index));
            currentMaterial = MaterialHelper.getGrayscaleMaterial(getBrightness(index,pathVertices.size()), assetManager);
            currentSeg = PathRenderHelper.createLineFromVertices(currentPath, currentMaterial);
            outputNode.attachChild(currentSeg);
            currentPath.remove(0);
        }
        
        return outputNode;
    }
    
    public static float getBrightness(float index, float totalLength){
        
        /*
         * The equation y=-4(x-0.5)^2 + 1
         *      gets us a parabola where it's
         *      peak is at (0.5,1) and the y value 
         *      is 0 at 0 and 1
         */
        
        float xVal = index/totalLength;
        float termToSquare = xVal-0.5f;
        return -4*termToSquare*termToSquare + 1;
        
    }
    
    
}
