/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.locationTracking;

import com.jme3.math.Vector3f;

/**
 *
 * @author BLI
 */
public class PointData {
    
    protected Vector3f pointCoords;
    
    public PointData(Vector3f coords){
        pointCoords = coords;
    }

    public Vector3f getPointCoords() {
        return pointCoords;
    }
    
    
    
}
