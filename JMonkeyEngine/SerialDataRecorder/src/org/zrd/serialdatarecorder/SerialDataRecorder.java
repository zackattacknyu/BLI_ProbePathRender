/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialdatarecorder;

import com.jme3.math.Matrix3f;
import org.zrd.serialReading.dataInterpretation.SerialDataInterpreter;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        
        Properties dataRecorderProperties = Properties_SerialDataRecorder.getProperties();
        Path filePath = Paths.get(dataRecorderProperties.getProperty("pathRecording.filePath"));
        final SerialDataInterpreter serialData = new SerialDataInterpreter(dataRecorderProperties);
        
        Thread t = new Thread(){
            public void run(){
                while(true){
                    try {
                        Thread.sleep(30);
                        serialData.updateData();
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
            }
        };
                
        System.out.println("Press Enter to begin calibration:");
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
        t.start();
        serialData.startStopCalibration();
        System.out.println("Press Enter to end calibration:");
        sc.nextLine();
        serialData.startStopCalibration();
        System.out.println("Press Enter to begin path recording");
        sc.nextLine();
        serialData.startStopRecording(filePath);
        System.out.println("Press Enter to end path recording");
        sc.nextLine();
        serialData.startStopRecording(filePath);
    }
    
    
}
