/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.signalProcessRecorder;

import org.zrd.util.dataStreaming.DataStreamRecorder;

/**
 *
 * @author Zach
 */
public class SignalProcessingRecorder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        DataStreamRecorder.startStreamingService(
                ProbeTracker_SignalProcessRecorder.
                createNewProbeTracker());
    }
}
