/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.meshTraversal;

import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 * This houses the code that takes in a mesh and a path
 *      as well as the target start and end points
 *      on the mesh and tries to make the path follow 
 *      the mesh so that the end points match
 *
 * @author BLI
 */
public class RotationCalibration {
    
    //first path inputted
    private ArrayList<Vector3f> originalPath;
    
    //path moved so that its start point is the same as target start point
    private ArrayList<Vector3f> initiallyMovedPath;
}
