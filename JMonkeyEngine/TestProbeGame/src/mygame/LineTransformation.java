/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Matrix3f;
import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author BLI
 */
public class LineTransformation {
 
    
    private Vector3f startingPt, expectedEndPt, actualEndPt;
    
    private Vector3f expectedDir,actualDir;
    
    private float rotAngle;
    private Vector3f rotAxis;
    
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
        makeRotationParameters();
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
        expectedDir = expectedEndPt.subtract(startingPt);
        actualDir = actualEndPt.subtract(startingPt);
        
        expectedDir = expectedDir.normalize();
        actualDir = actualDir.normalize();
    }
    
    private void makeRotationParameters(){
        float cosTheta = expectedDir.dot(actualDir);
        rotAngle = (float)Math.acos(cosTheta);
        
        rotAxis = expectedDir.cross(actualDir);
        rotAxis = rotAxis.normalize();
    }
    
    private void makeRotationQuat(){
        rotQuaternion = new Quaternion();
        rotQuaternion.fromAngleAxis(rotAngle, rotAxis);
    }
    
    private void makeTranslationMatrices(){
        firstTranslation = Matrix4f.IDENTITY;
        lastTranslation = Matrix4f.IDENTITY;
        
        firstTranslation.setTranslation(startingPt.mult(-1));
        lastTranslation.setTranslation(startingPt);
    }
    
    private void makeWholeTransformation(){
        Matrix4f rotTransform = Matrix4f.IDENTITY;
        rotTransform.setRotationQuaternion(rotQuaternion);
        wholeTransformation = (firstTranslation.mult(rotTransform)).mult(lastTranslation);
    }
    
    private void makeOutput(){
        outputRotation = wholeTransformation.toRotationQuat();
        outputTranslation = wholeTransformation.toTranslationVector();
    }
    
}
