/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author BLI
 */
public class ProbeDataWriter {
    
    private String fileName;
    private File outputFile;
    private FileWriter outputFileWriter;
    private BufferedWriter outputWriter;
    
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
    
}
