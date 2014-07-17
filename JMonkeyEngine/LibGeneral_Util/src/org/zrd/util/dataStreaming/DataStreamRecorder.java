/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.dataStreaming;

import java.util.Scanner;

/**
 *
 * @author BLI
 */
public class DataStreamRecorder {

    public static void startStreamingService(final ProbeDataStream dataStream) {
        Thread t = new Thread() {
            @Override
            public void run() {
                while (true) {
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
        while (true) {
            System.out.println("Press Enter to begin recording");
            sc.nextLine();
            dataStream.startStopRecording();
            System.out.println("Press Enter to end recording");
            sc.nextLine();
            dataStream.startStopRecording();
        }
    }
    
}
