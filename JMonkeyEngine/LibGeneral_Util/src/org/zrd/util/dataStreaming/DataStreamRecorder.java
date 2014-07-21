/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.dataStreaming;

import java.util.Scanner;

/**
 * This is meant to be used by a command-line application to 
 *      begin streaming of data and then being and end 
 *      recording of data.
 * It takes in a data stream and then controls when it starts
 *      as well as when the recording stops and starts
 * Examples of data streams include raw probe data, segemented probe data,
 *      or location data.
 *
 * @author BLI
 */
public class DataStreamRecorder {

    /**
     * Starts the streaming service and then presents the user 
     *      with options to start and stop the recording
     * @param dataStream    The data stream class
     */
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
