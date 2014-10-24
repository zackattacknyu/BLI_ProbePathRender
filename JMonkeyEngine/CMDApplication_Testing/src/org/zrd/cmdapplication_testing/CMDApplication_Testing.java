/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.cmdapplication_testing;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

import com.jme3.math.Vector2f;
import java.io.IOException;
import java.nio.file.*;
import java.io.File;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometryUtil.GeometryDataHelper;
import org.zrd.util.fileHelper.FilePathHelper;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

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
        
        //filter out (0,0) coords
        while(texCoords.remove(Vector2f.ZERO));
        
        File textureFile = inputFilePath.resolve("sampleTexture.png").toFile();
        BufferedImage image = ImageIO.read(textureFile);
        
        Graphics2D imageGraphics = image.createGraphics();
        BasicStroke stroke = new BasicStroke(2.0f);
        imageGraphics.setStroke(stroke);
        imageGraphics.setColor(Color.RED);
        
        float x1,y1,x2,y2;
        Vector2f vert1,vert2;
        for(int index = 1; index < texCoords.size(); index++){
            vert1 = texCoords.get(index);
            vert2 = texCoords.get(index-1);
            
            x1 = vert1.getX()*image.getWidth();
            x2 = vert2.getX()*image.getWidth();
            
            y1 = (1-vert1.getY())*image.getHeight();
            y2 = (1-vert2.getY())*image.getHeight();
            
            //change line color
            if(index == 200){
                imageGraphics.setColor(Color.BLUE);
            }
            
            imageGraphics.draw(new Line2D.Float(x1, y1, x2, y2));
        }
        
        
        BufferedImage bi = image;
        File outputfile = FilePathHelper.getDefaultOutputFolder().resolve("sampleOutput.png").toFile();
        ImageIO.write(bi, "png", outputfile);
    }

    

}
