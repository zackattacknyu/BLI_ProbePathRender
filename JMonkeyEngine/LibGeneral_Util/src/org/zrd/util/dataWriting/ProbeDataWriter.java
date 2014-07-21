/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.dataWriting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.zrd.util.timeTools.TimeHelper;

/**
 * This is a class for writing probe data. It is meant to do the work
 *      once you have a path to put the file as well as strings to put
 *      into the text file. 
 * 
 * The format of probe data files in this case is as follows:
 *      {Prefix for what type of data}_{timestamp}.txt
 *
 * @author BLI
 */
public class ProbeDataWriter {

    //the file object for the output file
    private File outputFile;
    
    //the file writer
    private FileWriter outputFileWriter;
    
    //the buffered file writer
    private BufferedWriter outputWriter;
    
    /**
     * This instantes a probe data writer. Specifically, it takes in the folder
     *      path for the file, the prefix for it, and makes the file name and 
     *      writer that will write data to the file. The file name will have the
     *      format {prefix}_{timestamp}.txt
     * @param folderPath        the folder path to put the file
     * @param fileNamePrefix    the prefix of the file
     * @throws IOException      if an IOException
     */
    public ProbeDataWriter(Path folderPath, String fileNamePrefix) throws IOException{
        outputFile = getNewDataFilePath(folderPath,fileNamePrefix).toFile();
        outputFileWriter = new FileWriter(outputFile);
        outputWriter = new BufferedWriter(outputFileWriter);
    }
    
    /**
     * This writ
     * @param line
     * @throws IOException 
     */
    public void writeLine(String line) throws IOException{
        outputWriter.write(line);
        outputWriter.newLine();
    }
    
    public void closeWriter() throws IOException{
        outputWriter.close();
        outputFileWriter.close();
    }
    
    public static ProbeDataWriter getNewWriter(Path folderPath, String prefix){
        try {
            ProbeDataWriter currentWriter = new ProbeDataWriter(folderPath,prefix);
            return currentWriter;
        } catch (IOException ex) {
            System.out.println("Exception thrown try to instantiate writer: " + ex);
            return null;
        }
    }
    
    public static Path getNewDataFilePath(Path folderPath, String fileNamePrefix) throws IOException{
        String currentTimestamp = TimeHelper.getTimestampSuffix();
        String fileName = fileNamePrefix + "_" + currentTimestamp + ".txt";
        if(!Files.exists(folderPath)){
            Files.createDirectories(folderPath);
        }
        return folderPath.resolve(fileName);
    }
    
    
    
    public static ProbeDataWriter closeWriter(ProbeDataWriter currentWriter){
        try {
            currentWriter.closeWriter();
        } catch (IOException ex) {
            System.out.println("Exception thrown trying to close writer: " + ex);
        }
        return null;
    }
    
    public static void writeLineInWriter(ProbeDataWriter writer, String line){
        try {
            writer.writeLine(line);
        } catch (IOException ex) {
            System.out.println("Exception thrown trying to write line to writer: " + ex);
        }
    }
    
}
