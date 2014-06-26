/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialdatarecorder;

import org.zrd.serialReading.dataInterpretation.SerialDataInterpreter;

/**
 *
 * @author BLI
 */
public class SerialDataRecorder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SerialDataInterpreter serialData = new SerialDataInterpreter();
        String currentString, previousString = "n";
        while(true){
            currentString = String.valueOf(serialData.getCurrentSerialOutput());
            if(!currentString.equals(previousString)){
                System.out.println(currentString);
            }
            previousString = currentString;
        }
    }
}
