/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.fileHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import javax.swing.JFileChooser;

/**
 *
 * @author BLI
 */
public class GeneralFileHelper {
    
    public static void createDirectoryIfNone(Path path){
        if(!Files.exists(path)){
            try {
                Files.createDirectories(path);
            } catch (IOException ex) {
                System.out.println("Error creating path: " + ex);
            }
        }
    }

    /**
     * This opens the file chooser window and puts into a file object
     *      the one selected if one was selected
     * @param initialImportDirectory        the directory for the file chooser window to open on
     * @return      the file that was chosen if one was chosen
     */
    public static File importPathUsingFileSelector(File initialImportDirectory) {
        JFileChooser selector = new JFileChooser(initialImportDirectory);
        int chosenOption = selector.showOpenDialog(null);
        File selectedFile = null;
        if (chosenOption == JFileChooser.APPROVE_OPTION) {
            selectedFile = selector.getSelectedFile();
        }
        return selectedFile;
    }

    /**
     * This imports a file and then copies it into the target
     *      directory
     * @param importDirectory       directory to put in file chooser window
     * @param targetDirectory       directory to copy file to
     * @return                      the copied file
     */
    public static File importAndCopyFile(File importDirectory, Path targetDirectory) {
        File fileToCopy = GeneralFileHelper.importPathUsingFileSelector(importDirectory);
        String fileName = fileToCopy.getName();
        Path copiedFilePath = targetDirectory.resolve(fileName);
        try {
            Files.copy(fileToCopy.toPath(), copiedFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return copiedFilePath.toFile();
    }
    
}
