/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.dataStreaming;

/**
 * This is a general interface which represents a stream of data
 *      that requires an update call and which has recording
 *      capability. 
 *
 * @author BLI
 */
public interface ProbeDataStream {

    /**
     * Updates the current data in the stream
     */
    void updateData();
    
    /**
     * starts or stops the recording of the stream
     */
    void startStopRecording();
}
