/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.fileHelper;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Zach
 */
public class FilePathHelper {
    
    public static final String DEFAULT_INPUT_FOLDER_NAME = "input";
    public static final String DEFAULT_OUTPUT_FOLDER_NAME = "output";
    
    public static Path getDefaultInputFolder(){
        return Paths.get(DEFAULT_INPUT_FOLDER_NAME);
    }
    
    public static Path getDefaultOutputFolder(){
        return Paths.get(DEFAULT_OUTPUT_FOLDER_NAME);
    }
}
