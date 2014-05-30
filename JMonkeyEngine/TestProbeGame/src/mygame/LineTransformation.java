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
        
        makeDirectionVectors();
        makeRotationQuat();
        makeTranslationMatrices();
        makeWholeTransformation();
        makeOutput();
    }

    public Quaternion getOutputRotation() {
        return outputRotation;
    }

    public Vector3f getOutputTranslation() {
        return outputTranslation;
    }
    
    
    
    private void makeDirectionVectors(){
        expectedDir = expectedEndPt.clone().subtract(startingPt);
        actualDir = actualEndPt.clone().subtract(startingPt);
        
        expectedDir = expectedDir.normalize();
        actualDir = actualDir.normalize();
    }
    
    private void makeRotationQuat(){
        rotQuaternion = MeshHelper.getRotationFromVectors(actualDir, expectedDir);
    }
    
    private void makeTranslationMatrices(){
        firstTranslation = new Matrix4f();
        lastTranslation = new Matrix4f();
        
        firstTranslation.setTranslation(startingPt);
        Vector3f startingPtNeg = startingPt.clone().negate();
        lastTranslation.setTranslation(startingPtNeg);
    }
    
    private void makeWholeTransformation(){
        Matrix4f rotTransform = new Matrix4f();
        rotTransform.setRotationQuaternion(rotQuaternion);
        wholeTransformation = (firstTranslation.mult(rotTransform)).mult(lastTranslation);
    }
    
    private void makeOutput(){
        outputRotation = wholeTransformation.toRotationQuat();
        outputTranslation = wholeTransformation.toTranslationVector();
        
        
    }
    
    public ArrayList<Vector3f> transformVertices(ArrayList<Vector3f> inputVertices){
        ArrayList<Vector3f> outputVertices = new ArrayList<Vector3f>(inputVertices.size());
        for(Vector3f vertex:inputVertices){
            outputVertices.add(wholeTransformation.mult(vertex));
        }
        return outputVertices;
    }
    
}
