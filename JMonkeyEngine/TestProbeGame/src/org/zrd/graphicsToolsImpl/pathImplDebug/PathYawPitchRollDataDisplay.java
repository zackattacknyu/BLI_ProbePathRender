/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.graphicsToolsImpl.pathImplDebug;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import java.io.File;
import java.util.ArrayList;
import org.zrd.graphicsToolsImpl.pathImpl.PathDataDisplay;
import org.zrd.probeTracking.deviceToWorldConversion.TrackingHelper;

/**
 *
 * @author BLI
 */
public class PathYawPitchRollDataDisplay extends PathDataDisplay{

    private ArrayList<Float> yawValues,pitchValues,rollValues;
    
    /**
     * This is used to be the start point on the sphere that will get
     *      rotated by each quaternion in the raw data.
     */
    public static final Vector3f START_POINT_ON_SPHERE = new Vector3f(1,0,0);
    
    private PathYawPitchRollDataDisplay(File initDir){
        super(initDir);
    }
    
    public static PathYawPitchRollDataDisplay obtainYawPitchRollProbeData(File initDir){
        PathYawPitchRollDataDisplay data = new PathYawPitchRollDataDisplay(initDir);
        if(data.isNullReturn()){
            return null;
        }else{
            return data;
        }
    }
    
    protected void generateDisplayValues(){
        
        displayVertices = new ArrayList<Vector3f>(10000);
        Quaternion currentQuat;
        Vector3f currentPt;
        for(int index = 0; index < yawValues.size(); index++){
            currentQuat = TrackingHelper.getQuarternion(
                    yawValues.get(index), 
                    pitchValues.get(index), 
                    rollValues.get(index));
            currentPt = currentQuat.toRotationMatrix().mult(START_POINT_ON_SPHERE);
            displayVertices.add(currentPt);
        }
        
    }

    @Override
    protected void addParts(String[] parts) {
        yawValues.add(Float.parseFloat(parts[0]));
        pitchValues.add(Float.parseFloat(parts[1]));
        rollValues.add(Float.parseFloat(parts[2]));
    }

    @Override
    protected void initializeArrayLists() {
        yawValues = new ArrayList<Float>(10000);
        pitchValues = new ArrayList<Float>(10000);
        rollValues = new ArrayList<Float>(10000);
    }

    @Override
    public Spatial generateReferenceObject(Material mat) {
        Sphere refSphere = new Sphere(40,40,1.0f);
        Spatial theSphere = new Geometry("sphere",refSphere);
        mat.getAdditionalRenderState().setWireframe(true);
        theSphere.setMaterial(mat);
        return theSphere;
    }
    
    
    
    
    
    
}
