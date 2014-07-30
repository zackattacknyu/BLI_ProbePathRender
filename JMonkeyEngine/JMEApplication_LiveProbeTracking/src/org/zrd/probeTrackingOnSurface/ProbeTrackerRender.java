/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTrackingOnSurface;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import org.zrd.geometryToolkit.locationTracking.LocationTracker;
import org.zrd.jmeGeometryIO.pathIO.PathRenderHelper;

/**
 *
 * @author BLI
 */
public class ProbeTrackerRender {
    
    private LocationTracker activeTracker;
    private Vector3f lastPosition;
    private Spatial probeObject;
    private Node renderedPaths;
    private Material lineMaterial;
    private boolean newRenderedPathsExist;
    
    public static final float MIN_ARC_LENGTH_FOR_RENDER = 0.5F;
    
    public ProbeTrackerRender(LocationTracker activeTracker, Spatial probeObject, Material lineMaterial){
        this.activeTracker = activeTracker;
        this.probeObject = probeObject;
        lastPosition = activeTracker.getCurrentPosition().clone();
        this.renderedPaths = new Node();
        this.lineMaterial = lineMaterial;
    }
    
    public void updateInfo(){
        activeTracker.updateData();
    }
    
    public void updateRenderObjectInfo(){
        probeObject.setLocalTranslation(activeTracker.getCurrentPosition().clone());
        probeObject.setLocalRotation(activeTracker.getLocalRotation());
    }
    
    public void updateRenderPathInfo(){
        
        if(activeTracker.isRecordingPath() && (activeTracker.getArcLengthSinceLastRead() > MIN_ARC_LENGTH_FOR_RENDER)){
            renderedPaths.attachChild(
                    PathRenderHelper.createLineFromVertices(
                    activeTracker.getVerticesSinceLastRead(), 
                    lineMaterial));
            newRenderedPathsExist = true;
        }
    }

    public Node getRenderedPaths() {
        Node returnPaths = renderedPaths.clone(true);
        renderedPaths = new Node();
        newRenderedPathsExist = false;
        return returnPaths;
    }

    public boolean isNewRenderedPathsExist() {
        return newRenderedPathsExist;
    }
    
    
    
}
