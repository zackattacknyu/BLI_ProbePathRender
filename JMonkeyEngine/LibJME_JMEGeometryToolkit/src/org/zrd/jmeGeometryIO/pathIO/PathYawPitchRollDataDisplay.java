/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryIO.pathIO;

import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * This takes files that have yaw,pitch,roll data
 *      and makes a path on the sphere which represents
 *      where an initial vector would be rotated to
 *      based on those yaw, pitch, roll values. 
 * It is meant to be used for debugging purposes to make
 *      sure the yaw,pitch,roll values of a path make sense
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
    
    /**
     * Takes in the directory to look for files, tells the user
     *      to select a file, and then renders the yaw,pitch,roll data in the file
     *      selected and makes the reference sphere for it
     * @param initDir       initial directory to look for files
     * @return              yaw,pitch,roll data display object that contains render objects
     */
    public static PathYawPitchRollDataDisplay obtainYawPitchRollProbeData(File initDir){
        PathYawPitchRollDataDisplay data = new PathYawPitchRollDataDisplay(initDir);
        if(data.isNullReturn()){
            return null;
        }else{
            return data;
        }
    }
    
    /**
     * This takes in the yaw,pitch,roll and uses it to rotate the initial vector
     *      on the sphere. It takes the result and adds it to the list
     *      of display vertices
     */
    @Override
    protected void generateDisplayValues(){
        
        displayVertices = new ArrayList<Vector3f>(10000);
        Quaternion currentQuat;
        Vector3f currentPt;
        for(int index = 0; index < yawValues.size(); index++){
            currentQuat = getQuaternion(
                    yawValues.get(index), 
                    pitchValues.get(index), 
                    rollValues.get(index));
            currentPt = currentQuat.toRotationMatrix().mult(START_POINT_ON_SPHERE);
            displayVertices.add(currentPt);
        }
        
    }
    
    /**
     * This takes in yaw,pitch,roll and gets the quaternion we want to use
     *      for debugging purposes.
     * 
     * NOTE: The GeometryToolkit has a helper class with this same exact method.
     *      We are keeping this one here however in case we want to do any 
     *      changes for debugging purposes
     * 
     * @param yawInRadians      yaw at point
     * @param pitchInRadians    pitch at point
     * @param rollInRadians     roll at point
     * @return                  quaternion for that rotation
     */
    private Quaternion getQuaternion(float yawInRadians, 
            float pitchInRadians, float rollInRadians){
        
        Quaternion yaw = new Quaternion();
        yaw.fromAngleAxis(yawInRadians, Vector3f.UNIT_Z);
        
        Quaternion pitch = new Quaternion();
        pitch.fromAngleAxis(pitchInRadians, Vector3f.UNIT_X);
        
        Quaternion roll = new Quaternion();
        roll.fromAngleAxis(rollInRadians, Vector3f.UNIT_Y);
        
        return (yaw.mult(pitch)).mult(roll);
        
        
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

    /**
     * This generates a wireframe sphere so the different
     *      yaw,pitch,roll rotation results can be shown
     *      as the path progresses
     * @param mat       material for the sphere
     * @return          jme spatial for the sphere
     */
    @Override
    public Spatial generateReferenceObject(Material mat) {
        Sphere refSphere = new Sphere(40,40,1.0f);
        Spatial theSphere = new Geometry("sphere",refSphere);
        mat.getAdditionalRenderState().setWireframe(true);
        theSphere.setMaterial(mat);
        return theSphere;
    }
    
    
    
    
    
    
}
