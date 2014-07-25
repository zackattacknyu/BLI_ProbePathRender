/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.bliProbePath;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author BLI
 */
public class Paths_BLIProbePath {
    
    public static final Path LOG_PARENT_PATH = Paths.get("textFiles").resolve("logs");
    public static final Path PATH_RECORDING_PATH = LOG_PARENT_PATH.resolve("paths");
    public static final Path CALIBRATION_RESULTS_PATH = LOG_PARENT_PATH.resolve("calibrationResults");
    
}
