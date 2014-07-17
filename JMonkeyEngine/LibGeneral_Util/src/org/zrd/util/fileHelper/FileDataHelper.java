/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.fileHelper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import javax.swing.JFileChooser;

/**
 *
 * @author BLI
 */
public class FileDataHelper {
    
    public static void exportLinesToFile(ArrayList<String> lines, Path dataFile){
        try {
            Files.write(dataFile, lines, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            System.out.println("Error writing lines to file: " + ex);
        }
    }

    public static File importPathUsingFileSelector(File initialImportDirectory) {
        JFileChooser selector = new JFileChooser(initialImportDirectory);
        int chosenOption = selector.showOpenDialog(null);
        File selectedFile = null;
        if (chosenOption == JFileChooser.APPROVE_OPTION) {
            selectedFile = selector.getSelectedFile();
        }
        return selectedFile;
    }

    public static ArrayList<String> getLinesFromFile(File dataFile) {
        ArrayList<String> lines = new ArrayList<String>();
        try {
            lines = (ArrayList<String>) Files.readAllLines(dataFile.toPath(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return lines;
    }
    
}
