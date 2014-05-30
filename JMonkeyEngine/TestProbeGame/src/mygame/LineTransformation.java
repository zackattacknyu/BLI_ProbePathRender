/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Matrix3f;
import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import meshTraversal.MeshHelper;

/**
 *
 * @author BLI
 */
public class LineTransformation {
 
    
    private Vector3f startingPt, expectedEndPt, actualEndPt;
    
    private Vector3f expectedDir,actualDir;
    
    private Quaternion rotQuaternion;
    private Matrix4f firstTranslation,lastTranslation;
    private Matrix4f wholeTransformation;
    
    private Quaternion outputRotation;
    private Vector3f outputTranslation;
    
    public LineTransformation(Vector3f startingPt, Vector3f expectedEndPt, Vector3f actualEndPt){
        this.startingPt = startingPt;
        this.expectedEndPt = expectedEndPt;
        this.actualEndPt = actualEndPt;

        makeWholeTransformation();
        makeOutput();
    }

    public Quaternion getOutputRotation() {
        return outputRotation;
    }

    public Vector3f getOutputTranslation() {
        return outputTranslation;
    }
    
    private void makeWholeTransformation(){
        wholeTransformation = MeshHelper.getRotationAroundPoint(startingPt, expectedEndPt, actualEndPt);
    }
    
    private void makeOutput(){
        outputRotation = wholeTransformation.toRotationQuat();
        outputTranslation = wholeTransformation.toTranslationVector();
        
        
    }
    
    public ArrayList<Vector3f> transformVertices(ArrayList<Vector3f> inputVertices){
        return MeshHelper.getTransformedVertices(inputVertices, wholeTransformation);
    }
    
}
