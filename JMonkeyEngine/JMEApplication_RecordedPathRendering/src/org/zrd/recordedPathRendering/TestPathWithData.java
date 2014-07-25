/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.recordedPathRendering;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.ArrayList;
import org.zrd.geometryToolkit.pathDataStructure.SegmentSet;
import org.zrd.jmeGeometry.pathRendering.PathRenderHelper;

/**
 *
 * @author BLI
 */
public class TestPathWithData {
    
    public static Node getPathSpatialWithSampleData(ArrayList<Vector3f> pathVertices, AssetManager assetManager){
        
        ArrayList<Float> dataAtVertices = new ArrayList<Float>(pathVertices.size());
        dataAtVertices.add(0f);
      
        for(int index = 1; index < pathVertices.size()-1;index++){
            dataAtVertices.add(getBrightness(index,pathVertices.size()));
        }
        
        SegmentSet pathSegment = new SegmentSet(pathVertices,dataAtVertices);
        
        return PathRenderHelper.createLineFromVerticesWithData(pathSegment, assetManager);
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
