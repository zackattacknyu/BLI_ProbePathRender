/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.qualityRecorder;

import org.zrd.util.dataStreaming.DataStreamRecorder;

/**
 *
 * @author BLI
 */
public class QualityRecorder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DataStreamRecorder.startStreamingService(
                QualityReader_QualityRecorder.createQualityReader());
    }
}
