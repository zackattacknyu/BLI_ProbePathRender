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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zrd.util.timeTools.TimeHelper;

/**
 *
 * @author BLI
 */
public class ProbeDataWriter {
    
    private String fileName;
    private File outputFile;
    private FileWriter outputFileWriter;
    private BufferedWriter outputWriter;
    
    public static ProbeDataWriter getNewWriter(Path folderPath, String prefix){
        String currentTimestamp = TimeHelper.getTimestampSuffix();
        try {
            ProbeDataWriter currentWriter = new ProbeDataWriter(folderPath,prefix,currentTimestamp);
            return currentWriter;
        } catch (IOException ex) {
            System.out.println("Exception thrown try to instantiate writer: " + ex);
            return null;
        }
    }
    
    public ProbeDataWriter(Path folderPath, String fileNamePrefix, String timestampSuffix) throws IOException{
        this.fileName = fileNamePrefix + "_" + timestampSuffix + ".txt";
        if(!Files.exists(folderPath)){
            Files.createDirectories(folderPath);
        }
        outputFile = folderPath.resolve(fileName).toFile();
        outputFileWriter = new FileWriter(outputFile);
        outputWriter = new BufferedWriter(outputFileWriter);
    }
    
    
    public void writeLine(String line) throws IOException{
        outputWriter.write(line);
        outputWriter.newLine();
    }
    
    public void closeWriter() throws IOException{
        outputWriter.close();
        outputFileWriter.close();
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
