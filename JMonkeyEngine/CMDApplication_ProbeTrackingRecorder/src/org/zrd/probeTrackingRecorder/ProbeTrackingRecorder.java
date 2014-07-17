/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTrackingRecorder;

import org.zrd.util.dataStreaming.DataStreamRecorder;
/**
 *
 * @author BLI
 */
public class ProbeTrackingRecorder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        DataStreamRecorder.startStreamingService(
                ProbeTracker_ProbeTrackingRecorder.
                createNewProbeTracker());
    }
    
    
}
