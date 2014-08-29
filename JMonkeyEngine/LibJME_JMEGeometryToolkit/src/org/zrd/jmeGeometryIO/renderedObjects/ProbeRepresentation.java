/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryIO.renderedObjects;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import org.zrd.jmeGeometryIO.pathIO.PathRenderHelper;
import org.zrd.jmeUtil.materials.MaterialHelper;

/**
 * 
 * This generates the probe representation for use
 *      by JME applications
 *
 * @author BLI
 */
public class ProbeRepresentation {
    
    private Node probeRep;
    
    /*
     * The magnitude of the lines that point in the positive/negative
     *      displacement directions. 
     * Magnitude here means multiple of the scale factor
     */
    private static final float POSITIVE_LINE_MAGNITUDE = 100000;
    private static final float NEGATIVE_LINE_MAGNITUDE = -8000;
    
    private ProbeRepresentation(AssetManager assetManager, float scaleX, float scaleY, float scaleNormal){
        
        Material redMaterial = MaterialHelper.getColorMaterial(assetManager,ColorRGBA.Red);
        Material orangeMaterial = MaterialHelper.getColorMaterial(assetManager,ColorRGBA.Orange);
        Material greenMaterial = MaterialHelper.getColorMaterial(assetManager,ColorRGBA.Green);
        
        
        Spatial xAxisLine = initXLine(redMaterial,scaleX);
        Spatial yAxisLine = initYLine(orangeMaterial,scaleY);
        Spatial zAxisLine = initZLine(greenMaterial,scaleNormal);
        
        probeRep = new Node("probeRep");
        probeRep.attachChild(xAxisLine);
        probeRep.attachChild(yAxisLine);
        probeRep.attachChild(zAxisLine);
        
    }
    
    public static Node getProbeRepresentation(AssetManager assetManager, float scaleX, float scaleY, float scaleNormal){
        ProbeRepresentation probe = new ProbeRepresentation(assetManager,scaleX,scaleY,scaleNormal);
        return probe.probeRep;
    }
    
    private static float getPositiveValue(float scale){
        return POSITIVE_LINE_MAGNITUDE*scale;
    }
    private static float getNegativeValue(float scale){
        return NEGATIVE_LINE_MAGNITUDE*scale;
    }
    
    private Spatial initXLine(Material ballMat, float scaleX){
        ArrayList<Vector3f> xLineVertices = new ArrayList<>();
        xLineVertices.add(new Vector3f(getPositiveValue(scaleX),0,0));
        xLineVertices.add(new Vector3f(getNegativeValue(scaleX),0,0));
        return PathRenderHelper.createLineFromVertices(xLineVertices,ballMat);
    }
    
    private Spatial initYLine(Material ballMat, float scaleY){
        ArrayList<Vector3f> yLineVertices = new ArrayList<>();
        yLineVertices.add(new Vector3f(0,getPositiveValue(scaleY),0));
        yLineVertices.add(new Vector3f(0,getNegativeValue(scaleY),0));
        return PathRenderHelper.createLineFromVertices(yLineVertices,ballMat);
    }
    
    private Spatial initZLine(Material ballMat, float scaleNormal){
        ArrayList<Vector3f> zLineVertices = new ArrayList<>();
        zLineVertices.add(new Vector3f(0,0,getPositiveValue(scaleNormal)));
        zLineVertices.add(new Vector3f(0,0,getNegativeValue(scaleNormal)));
        return PathRenderHelper.createLineFromVertices(zLineVertices,ballMat);
    }
    
}
