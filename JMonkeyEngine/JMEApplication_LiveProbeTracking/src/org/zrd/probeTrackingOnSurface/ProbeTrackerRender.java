/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTrackingOnSurface;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometryUtil.ProgramConstants;
import org.zrd.geometryToolkit.locationTracking.LocationTracker;
import org.zrd.graphicsToolsImpl.pathImpl.PathRenderHelper;

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
    
    public ProbeTrackerRender(LocationTracker activeTracker, Spatial probeObject, Material lineMaterial){
        this.activeTracker = activeTracker;
        this.probeObject = probeObject;
        lastPosition = activeTracker.getCurrentPosition();
        this.renderedPaths = new Node();
        this.lineMaterial = lineMaterial;
    }
    
    public void updateInfo(){
        activeTracker.updateData();
        
        Vector3f currentPosition = activeTracker.getCurrentPosition().clone();
        if(currentPosition.distance(lastPosition) > ProgramConstants.MIN_SEGMENT_LENGTH){
            if(activeTracker.isRecordingPath()){
                addSegment(currentPosition,lastPosition);
            }
            
            lastPosition = currentPosition.clone();
        }

        probeObject.setLocalTranslation(currentPosition);
        probeObject.setLocalRotation(activeTracker.getLocalRotation());
    }
    
    private void addSegment(Vector3f vertex1, Vector3f vertex2){
        ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
        vertices.add(vertex1.clone());
        vertices.add(vertex2.clone());
        renderedPaths.attachChild(PathRenderHelper.createLineFromVertices(vertices, lineMaterial));
        newRenderedPathsExist = true;
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
