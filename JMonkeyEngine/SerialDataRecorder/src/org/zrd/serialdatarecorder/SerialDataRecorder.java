/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialdatarecorder;

import com.jme3.math.Matrix3f;
import org.zrd.probeTracking.TrackingHelper;
import org.zrd.serialReading.dataInterpretation.SerialDataInterpreter;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
/**
 *
 * @author BLI
 */
public class SerialDataRecorder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //doRotationTesting();
        
        SerialDataInterpreter serialData = new SerialDataInterpreter();
        String currentString, previousString = "n";
        while(true){
            serialData.updateData();
            currentString = String.valueOf(serialData.getCurrentSerialOutput());
            if(!currentString.equals(previousString)){
                System.out.println(currentString);
            }
            previousString = currentString;
        }
    }
    
    public static void doRotationTesting(){
        float yaw = (float)(Math.random()*(Math.PI/2.0));
        float pitch = (float)(Math.random()*(Math.PI/2.0));
        float roll = (float)(Math.random()*(Math.PI/2.0));
        
        Quaternion yawQ = new Quaternion();
        yawQ.fromAngleAxis(yaw, Vector3f.UNIT_Z);
        Matrix3f yawQMat = yawQ.toRotationMatrix();
        
        Quaternion pitchQ = new Quaternion();
        pitchQ.fromAngleAxis(pitch, Vector3f.UNIT_X);
        Matrix3f pitchQMat = pitchQ.toRotationMatrix();
        
        Quaternion rollQ = new Quaternion();
        rollQ.fromAngleAxis(roll, Vector3f.UNIT_Y);
        Matrix3f rollQMat = rollQ.toRotationMatrix();
        
        Quaternion quat = (yawQ.mult(pitchQ)).mult(rollQ);
        Matrix3f quatMat = quat.toRotationMatrix();
        Matrix3f rotMat = (yawQMat.mult(pitchQMat)).mult(rollQMat);
        
        System.out.println("(Yaw,Pitch,Roll)=(" + yaw + "," + pitch + "," + roll + ")");
        System.out.println("Rotation Matrix with quat " + quatMat);
        System.out.println("Rotation Matrix without quat: " + rotMat);
    }
}
