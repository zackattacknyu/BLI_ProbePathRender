/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialdatarecorder;

import static java.lang.Thread.sleep;
import org.zrd.serialDataInterpreter.dataInterpretation.SerialDataInterpreter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;
import org.zrd.util.dataStreaming.ProbeDataStream;
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
        
        beginStreaming(serialData);
    }
    
    public static void beginStreaming(final ProbeDataStream dataStream){
        
        Thread t = new Thread(){
            @Override
            public void run(){
                while(true){
                    try {
                        sleep(30);
                        dataStream.updateData();
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
            dataStream.startStopRecording();
            System.out.println("Press Enter to end recording");
            sc.nextLine();
            dataStream.startStopRecording();
        }
    }
    
    
}
