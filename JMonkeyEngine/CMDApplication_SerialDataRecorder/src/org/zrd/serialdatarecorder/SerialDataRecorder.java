/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialdatarecorder;

import org.zrd.util.dataStreaming.DataStreamRecorder;
/**
 *
 * @author BLI
 */
public class SerialDataRecorder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        DataStreamRecorder.startStreamingService(
                SerialInterpreter_SerialDataRecorder.
                createSerialInterpreter());
    }
    
    
}
