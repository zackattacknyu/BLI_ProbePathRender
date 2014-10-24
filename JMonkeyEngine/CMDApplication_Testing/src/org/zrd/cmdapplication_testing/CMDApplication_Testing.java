/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.cmdapplication_testing;

import com.jme3.math.Vector2f;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.io.File;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometryUtil.GeometryDataHelper;
import org.zrd.util.fileHelper.FilePathHelper;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.image.*;
import java.awt.Graphics2D;

/**
 *
 * @author BLI
 */
public class CMDApplication_Testing {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        
        Path inputFilePath = FilePathHelper.getDefaultInputFolder();
        String texCoordFileName = "textureCoordinateOutput_fromCenterToI.txt";
        File texCoordFile = inputFilePath.resolve(texCoordFileName).toFile();
        ArrayList<Vector2f> texCoords = GeometryDataHelper.get2DVerticesFromFile(texCoordFile);
        
        File textureFile = inputFilePath.resolve("sampleTexture.png").toFile();
        BufferedImage image = ImageIO.read(textureFile);
        
        Graphics2D imageGraphics = image.createGraphics();
        imageGraphics.draw3DRect(100, 100, 30, 30, true);
        
        BufferedImage bi = image;
        File outputfile = FilePathHelper.getDefaultOutputFolder().resolve("sampleOutput.png").toFile();
        ImageIO.write(bi, "png", outputfile);
    }

    

}
