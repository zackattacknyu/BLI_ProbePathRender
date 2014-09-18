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
import org.zrd.jmeGeometryIO.pathIO.FloatToRedBlueGradient;
import org.zrd.jmeGeometryIO.pathIO.PathRenderHelper;

/**
 *
 * @author BLI
 */
public class TestPathWithData {
    
    public static Node getPathSpatialWithSampleData(ArrayList<Vector3f> pathVertices, AssetManager assetManager){
        
        ArrayList<String[]> dataAtVertices = new ArrayList<String[]>(pathVertices.size());
        String[] data = {String.valueOf(0)};
        dataAtVertices.add(data);
      
        for(int index = 1; index < pathVertices.size()-1;index++){
            data = new String[1];
            data[0] = String.valueOf(getBrightness(index,pathVertices.size()));
            dataAtVertices.add(data);
        }
        
        SegmentSet pathSegment = new SegmentSet(pathVertices,dataAtVertices);
        
        return PathRenderHelper.createLineFromVerticesWithData(pathSegment, 
                assetManager, new FloatToRedBlueGradient());
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
