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
 * This contains methods that read and write data
 *      to a file
 *
 * @author BLI
 */
public class FileDataHelper {
    
    /**
     * This exports the array list of lines to a file
     * @param lines         the lines of the text file
     * @param dataFile      the file to put the lines into
     */
    public static void exportLinesToFile(ArrayList<String> lines, Path dataFile){
        try {
            Files.write(dataFile, lines, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            System.out.println("Error writing lines to file: " + ex);
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
     * This takes in a file and puts each line into a separate entry
     *      in an array list of strings
     * @param dataFile      file to read from
     * @return              array list of strings where each entry is a line in the file
     */
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
