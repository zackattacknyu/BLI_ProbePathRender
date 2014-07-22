/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataInterpretation;

import java.nio.file.Path;
import org.zrd.util.dataHelp.OutputHelper;
import org.zrd.util.dataWriting.ProbeDataWriter;

/**
 * This is used to record serial data and put 
 *      it into text files. It initializes
 *      the three writers for the three data files
 *      that will be generated, calls the methods
 *      that will write the data to those writers,
 *      and then closes the writers
 * 
 * The three text files generated are:
 *      1) XY Data File: each line is the form {x},{y} and 
 *          just includes the value straight from the serial data
 *      2) Orientation Data File: each line is in the form {yaw},{pitch},{roll}
 *          and includes the value in radians of those fields
 *      3) Output Data File: each line is the form
 *          {yaw},{pitch},{roll},{x},{y} so that it includes the data
 *          from both (1) and (2)
 *
 * @author BLI
 */
public class SerialDataRecorder {
    
    //the prefix for the file names of the files storing x,y data
    public static final String XY_DATA_FILES_PREFIX = "pathXYData";
    
    //the prefix for the file names of the files storing orientation data
    public static final String ORIENTATION_DATA_FILES_PREFIX = "yawPitchRollData";
    
    //the prefix for the file names of the files storing output data
    public static final String OUTPUT_DATA_FILES_PREFIX = "outputData";
    
    private ProbeDataWriter xyDataWriter;
    private ProbeDataWriter yawPitchRollDataWriter;
    private ProbeDataWriter outputDataWriter;

    /**
     * This initializes the data writers for the three text files that will be
     *      generated which have serial data in them. The three files
     *      are detailed in the description above
     * 
     * @param filePath      folder to put the text files generated
     */
    public SerialDataRecorder(Path filePath){
        xyDataWriter = ProbeDataWriter.getNewWriter(filePath, XY_DATA_FILES_PREFIX);
        yawPitchRollDataWriter = ProbeDataWriter.getNewWriter(filePath, ORIENTATION_DATA_FILES_PREFIX);
        outputDataWriter = ProbeDataWriter.getNewWriter(filePath, OUTPUT_DATA_FILES_PREFIX);
    }
    
    /**
     * This adds a line to each of the text files for the current data
     *      from the serial probe
     * 
     * @param deltaX        the current delta x value from the probe
     * @param deltaY        the current delta y value from the probe
     * @param yaw           the current yaw value from the probe
     * @param pitch         the current pitch value from the probe
     * @param roll          the current roll value from the probe
     */
    public void addLineToFiles(float deltaX, float deltaY, float yaw, float pitch, float roll){
        ProbeDataWriter.writeLineInWriter(xyDataWriter, 
                OutputHelper.getPositionOutputText(deltaX, deltaY));
        ProbeDataWriter.writeLineInWriter(yawPitchRollDataWriter, 
                OutputHelper.getOrientationOutputText(yaw, pitch, roll));
        ProbeDataWriter.writeLineInWriter(outputDataWriter, 
                OutputHelper.getOutputText(deltaX, deltaY, yaw, pitch, roll));
    }
    
    /**
     * This closes the recording of the three text files
     */
    public void closeRecording(){
        ProbeDataWriter.closeWriter(xyDataWriter);
        ProbeDataWriter.closeWriter(yawPitchRollDataWriter);
        ProbeDataWriter.closeWriter(outputDataWriter);
    }
    
    
}
