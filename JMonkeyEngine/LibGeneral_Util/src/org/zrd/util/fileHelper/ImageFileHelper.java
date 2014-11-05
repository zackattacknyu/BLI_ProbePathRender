/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.fileHelper;


import java.io.IOException;
import java.nio.file.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.util.Calendar;

/**
 *
 * @author BLI
 */
public class ImageFileHelper {
    
    public static void deleteTempImage(File imageFile){
        try {
            Files.delete(imageFile.toPath());
        } catch (IOException ex) {
            System.out.println("Error deleting temp image file: " + ex);
        }
    }
    
    public static void deleteTempImageFolder(){
        File tempImageFolder = FilePathHelper.getDefaultTempFolder().toFile();
        deleteTempImage(tempImageFolder);
    }
    
    public static File getTempImageFile(){
        Path tempFolder = FilePathHelper.getDefaultTempFolder();
        GeneralFileHelper.createDirectoryIfNone(tempFolder);
        
        long timestamp = Calendar.getInstance().getTimeInMillis();
        String fileName = "tempImage_" + timestamp + ".png";
        return tempFolder.resolve(fileName).toFile();
    }
    
    public static void writePNGimage(BufferedImage image, File fileToWrite){
        try {
            ImageIO.write(image, "png", fileToWrite);
        } catch (IOException ex) {
            System.out.println("Exception trying to write PNG image: " + ex);
        }
    }
    
    public static BufferedImage readPNGimage(File fileToRead){
        try {
            return ImageIO.read(fileToRead);
        } catch (IOException ex) {
            System.out.println("Exception trying to read PNG image: " + ex);
            return null;
        }
    }
    
}
