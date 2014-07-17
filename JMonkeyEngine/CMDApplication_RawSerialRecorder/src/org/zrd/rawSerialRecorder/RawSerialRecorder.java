/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.rawSerialRecorder;

import org.zrd.util.dataStreaming.DataStreamRecorder;
/**
 *
 * @author BLI
 */
public class RawSerialRecorder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        DataStreamRecorder.startStreamingService(
                SerialDataReader_RawSerialRecorder.createSerialReader());
    }
    
    
}
