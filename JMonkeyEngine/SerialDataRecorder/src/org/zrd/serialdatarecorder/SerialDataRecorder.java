/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialdatarecorder;

import org.zrd.serialDataInterpreter.dataInterpretation.SerialDataInterpreter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;
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
        final SerialDataInterpreter serialData = new SerialDataInterpreter(dataRecorderProperties,filePath);
        
        Thread t = new Thread(){
            @Override
            public void run(){
                while(true){
                    try {
                        sleep(30);
                        serialData.updateData();
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
            }
        };
                
        Scanner sc = new Scanner(System.in);
        System.out.println("Press Enter to Begin");
        sc.nextLine();
        t.start();
        
        while(true){
            System.out.println("Press Enter to begin recording");
            sc.nextLine();
            serialData.startStopRecording();
            System.out.println("Press Enter to end recording");
            sc.nextLine();
            serialData.startStopRecording();
        }
    }
    
    
}
