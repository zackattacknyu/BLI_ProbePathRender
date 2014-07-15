/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.bliProbePath.renderedObjects;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
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
public class ProbeRepresentation {
    
    private Node probeRep;
    
    public ProbeRepresentation(AssetManager assetManager){
        
        Material redMaterial = MaterialHelper.makeColorMaterial(assetManager,ColorRGBA.Red);
        Material orangeMaterial = MaterialHelper.makeColorMaterial(assetManager,ColorRGBA.Orange);
        Material greenMaterial = MaterialHelper.makeColorMaterial(assetManager,ColorRGBA.Green);
        
        
        Spatial xAxisLine = initXLine(redMaterial);
        Spatial yAxisLine = initYLine(orangeMaterial);
        Spatial zAxisLine = initZLine(greenMaterial);
        
        probeRep = new Node("probeRep");
        probeRep.attachChild(xAxisLine);
        probeRep.attachChild(yAxisLine);
        probeRep.attachChild(zAxisLine);
        
    }
    
    public static Node getProbeRepresentation(AssetManager assetManager){
        ProbeRepresentation probe = new ProbeRepresentation(assetManager);
        return probe.probeRep;
    }
    
    private Spatial initXLine(Material ballMat){
        ArrayList<Vector3f> xLineVertices = new ArrayList<Vector3f>();
        xLineVertices.add(new Vector3f(4f,0,0));
        xLineVertices.add(new Vector3f(-0.2f,0,0));
        return PathRenderHelper.createLineFromVertices(xLineVertices,ballMat);
    }
    
    private Spatial initYLine(Material ballMat){
        ArrayList<Vector3f> yLineVertices = new ArrayList<Vector3f>();
        yLineVertices.add(new Vector3f(0,4f,0));
        yLineVertices.add(new Vector3f(0,-0.2f,0));
        return PathRenderHelper.createLineFromVertices(yLineVertices,ballMat);
    }
    
    private Spatial initZLine(Material ballMat){
        ArrayList<Vector3f> zLineVertices = new ArrayList<Vector3f>();
        zLineVertices.add(new Vector3f(0,0,-4f));
        zLineVertices.add(new Vector3f(0,0,0.2f));
        return PathRenderHelper.createLineFromVertices(zLineVertices,ballMat);
    }
    
}
