/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BLI
 */
public class ProbeDataWriter {
    
    private String fileName;
    private File outputFile;
    private FileWriter outputFileWriter;
    private BufferedWriter outputWriter;
    
    public ProbeDataWriter(String fileName) throws IOException{
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy_MM_dd__kk_mm_ss");
        Calendar rightNow = Calendar.getInstance();
        String fileNameSuffix = myFormat.format(rightNow.getTime());
        this.fileName = fileName + "_" + fileNameSuffix + ".txt";
        initializeFile();
    }
    
    private void initializeFile() throws IOException{
        outputFile = new File(fileName);
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
