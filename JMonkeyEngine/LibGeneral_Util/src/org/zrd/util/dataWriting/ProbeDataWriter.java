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
import org.zrd.util.fileHelper.GeneralFileHelper;

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
        outputFile = GeneralFileHelper.getNewDataFilePath(folderPath,fileNamePrefix).toFile();
        outputFileWriter = new FileWriter(outputFile);
        outputWriter = new BufferedWriter(outputFileWriter);
    }
    
    /**
     * This writes a line to the current writer being output writer
     * @param line              line to writer to the text file
     * @throws IOException      if there is a problem writing the line
     */
    public void writeLine(String line) throws IOException{
        outputWriter.write(line);
        outputWriter.newLine();
    }
    
    /**
     * This closes the output stream and is meant to be called once the 
     *      writing of the file is finished
     * @throws IOException      if there is a problem closing the output stream
     */
    public void closeWriter() throws IOException{
        outputWriter.close();
        outputFileWriter.close();
    }

    public FileWriter getOutputFileWriter() {
        return outputFileWriter;
    }
    
    /**
     * This takes in the folder path and prefix and returns the data writer
     *      object from this class
     * @param folderPath        path to put file into
     * @param prefix            prefix of the data file name
     * @return                  data writer object
     */
    public static ProbeDataWriter getNewWriter(Path folderPath, String prefix){
        try {
            ProbeDataWriter currentWriter = new ProbeDataWriter(folderPath,prefix);
            return currentWriter;
        } catch (IOException ex) {
            System.out.println("Exception thrown try to instantiate writer: " + ex);
            return null;
        }
    }

    /**
     * This is a static method that closes the writer of the probe data writer
     *      object passed in and tells the user if something went wrong
     * @param currentWriter     the probe data writer to close
     */    
    public static void closeWriter(ProbeDataWriter currentWriter){
        try {
            currentWriter.closeWriter();
        } catch (IOException ex) {
            System.out.println("Exception thrown trying to close writer: " + ex);
        }
    }
    
    /**
     * This is a static method that takes in a data writer and writes a line
     *      to it, only if the writer has been instantiated first. 
     * @param writer        the probe data writer to write a line to
     * @param line          the line to writer to the data writer
     */
    public static void writeLineInWriter(ProbeDataWriter writer, String line){
        try {
            writer.writeLine(line);
        } catch (IOException ex) {
            System.out.println("Exception thrown trying to write line to writer: " + ex);
        }
    }
    
}
