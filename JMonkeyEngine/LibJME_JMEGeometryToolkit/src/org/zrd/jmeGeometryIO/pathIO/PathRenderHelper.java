/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryIO.pathIO;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometryUtil.GeometryDataHelper;
import org.zrd.geometryToolkit.pathDataStructure.SegmentSet;
import org.zrd.jmeUtil.ColorHelper;
import org.zrd.jmeUtil.materials.MaterialHelper;

/**
 * 
 * These classes take paths and make objects that JME can render
 *
 * @author BLI
 */
public class PathRenderHelper {
    public static final float PATH_LINE_WIDTH = 60.0F;
    public static final ColorRGBA LINE_COLOR = ColorRGBA.Black;
    
    public static final float IMAGE_LINE_STROKE_WIDTH = 2.0f;
    
    public static final float RADIUS_OF_DATA = 4F;
    
    public static BufferedImage createCirclesOnImage(BufferedImage image, SegmentSet lineWithData,StringToColorConversion converter){
        ArrayList<Vector2f> texCoordsFromData = lineWithData.getVertexTextureCoords();
        ArrayList<String[]> dataAtVerticesFromObj = lineWithData.getDataAtVertices();
        
        //makes copies so that we can delete bad entries
        ArrayList<Vector2f> texCoords = (ArrayList<Vector2f>) texCoordsFromData.clone();
        ArrayList<String[]> dataAtVertices = (ArrayList<String[]>) dataAtVerticesFromObj.clone();
        
        //deletes bad entries
        for(int index = 0; index < texCoords.size(); index++){
            if(texCoords.get(index).equals(GeometryDataHelper.getBadTexCoord())){
                /*System.out.println("BAD TEXTURE COORDINATES FOR TRIANGLE");
                if(index > 0){
                    System.out.println("Tex Coord Before: " + 
                            texCoords.get(index-1) + 
                            "; Tex Coord After: " + 
                            texCoords.get(index+1));
                }*/
                
                texCoords.remove(index);
                dataAtVertices.remove(index);
            }
        }
        
        Graphics2D imageGraphics = image.createGraphics();
        BasicStroke stroke = new BasicStroke(IMAGE_LINE_STROKE_WIDTH);
        imageGraphics.setStroke(stroke);
        
        float xCenter,yCenter,xUpperLeft,yUpperLeft;
        float diameter = 2*RADIUS_OF_DATA;
        Vector2f vertex;
        Color currentColor;
        for(int index = 0; index < texCoords.size(); index++){
            
            currentColor = ColorHelper.convertJMEcolorToJavaColor(
                    converter.convertStringToColor(dataAtVertices.get(index)));
            imageGraphics.setColor(currentColor);
            vertex = texCoords.get(index);
            
            xCenter = vertex.getX()*image.getWidth();
            yCenter = (1-vertex.getY())*image.getHeight();
            
            /*
             * (xCenter,yCenter) defines center of circle
             * 
             */
            xUpperLeft = xCenter-RADIUS_OF_DATA;
            yUpperLeft = yCenter-RADIUS_OF_DATA;
            
            imageGraphics.draw(new Ellipse2D.Float(xUpperLeft, yUpperLeft, diameter, diameter));
        }
        
        return image;
    }
    
    public static BufferedImage createLineOnImage(BufferedImage image, SegmentSet lineWithData,StringToColorConversion converter){
        ArrayList<Vector2f> texCoordsFromData = lineWithData.getVertexTextureCoords();
        ArrayList<String[]> dataAtVerticesFromObj = lineWithData.getDataAtVertices();
        
        //makes copies so that we can delete bad entries
        ArrayList<Vector2f> texCoords = (ArrayList<Vector2f>) texCoordsFromData.clone();
        ArrayList<String[]> dataAtVertices = (ArrayList<String[]>) dataAtVerticesFromObj.clone();
        
        //deletes bad entries
        for(int index = 0; index < texCoords.size(); index++){
            if(texCoords.get(index).equals(GeometryDataHelper.getBadTexCoord())){
                /*System.out.println("BAD TEXTURE COORDINATES FOR TRIANGLE");
                if(index > 0){
                    System.out.println("Tex Coord Before: " + 
                            texCoords.get(index-1) + 
                            "; Tex Coord After: " + 
                            texCoords.get(index+1));
                }*/
                
                texCoords.remove(index);
                dataAtVertices.remove(index);
            }
        }
        
        Graphics2D imageGraphics = image.createGraphics();
        BasicStroke stroke = new BasicStroke(IMAGE_LINE_STROKE_WIDTH);
        imageGraphics.setStroke(stroke);
        
        float x1,y1,x2,y2;
        Vector2f vert1,vert2;
        Color currentColor;
        for(int index = 1; index < texCoords.size(); index++){
            
            currentColor = ColorHelper.convertJMEcolorToJavaColor(
                    converter.convertStringToColor(dataAtVertices.get(index-1)));
            imageGraphics.setColor(currentColor);
            vert1 = texCoords.get(index);
            vert2 = texCoords.get(index-1);
            
            x1 = vert1.getX()*image.getWidth();
            x2 = vert2.getX()*image.getWidth();
            
            y1 = (1-vert1.getY())*image.getHeight();
            y2 = (1-vert2.getY())*image.getHeight();
            
            imageGraphics.draw(new Line2D.Float(x1, y1, x2, y2));
        }
        
        return image;
    }
    
    /**
     * This takes in a segment sets with data and returns a Node which is a set
     *      of line segment where each segment has a different color
     * 
     * @param lineWithData  the SegmentSet object
     * @param assetManager  the application's asset manager
     * @return              a Node containing all the rendered segments
     */
    public static Node createLineFromVerticesWithData(SegmentSet lineWithData, 
            AssetManager assetManager, StringToColorConversion converter){
        Node outputNode = new Node();
        Spatial currentSeg;
        Material currentMaterial;
        
        ArrayList<Vector3f> currentPath = new ArrayList<Vector3f>(2);
        ArrayList<Vector3f> pathVertices = lineWithData.getPathVertices();
        ArrayList<String[]> dataAtVertices = lineWithData.getDataAtVertices();
        System.out.println("Path Vertices Size: " + pathVertices.size());
        System.out.println("Data At Vertices size: " + dataAtVertices.size());
        
        currentPath.add(pathVertices.get(0).clone());
        
        //goes through each path vertices
        for(int index = 1; index < pathVertices.size()-1;index++){
            currentPath.add(pathVertices.get(index));

            currentMaterial = MaterialHelper.getColorMaterial(assetManager,
                    converter.convertStringToColor(dataAtVertices.get(index-1)));

            //creates the line using the vertices and color
            currentSeg = createLineFromVertices(currentPath, currentMaterial);
            
            //adds the rendered segment to the rendered node
            outputNode.attachChild(currentSeg);
            currentPath.remove(0);
        }
        
        return outputNode;
        
    }

    /**
     * This simply takes in a materal and line vertices and returns the JME
     *      object that it can render which shows the line
     * 
     * @param lineVertices  the vertices of the line
     * @param material      the material to overlay on the line
     * @return              the spatial representing the line that JME can render
     */
    public static Spatial createLineFromVertices(
            ArrayList<Vector3f> lineVertices, Material material) {
        
        //makes the index buffer
        short[] indices = new short[lineVertices.size() * 2];
        for (int index = 0; index < lineVertices.size() - 1; index++) {
            indices[2 * index] = (short) index;
            indices[2 * index + 1] = (short) (index + 1);
        }
        
        //makes the vertex array
        Vector3f[] lineVertexData = lineVertices.toArray(new Vector3f[lineVertices.size()]);
        
        //makes the color array
        ColorRGBA lineColor = LINE_COLOR;
        Vector4f[] lineColors = new Vector4f[lineVertices.size()];
        for (int j = 0; j < lineColors.length; j++) {
            lineColors[j] = new Vector4f(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), lineColor.getAlpha());
        }
        
        //makes a mesh which is how the path will be rendered
        Mesh mesh = new Mesh();
        mesh.setMode(Mesh.Mode.Lines);
        
        //uses the arrays constructed above and puts them in the mesh
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(lineVertexData));
        mesh.setBuffer(VertexBuffer.Type.Index, 2, indices);
        mesh.setBuffer(VertexBuffer.Type.Color, 4, BufferUtils.createFloatBuffer(lineColors));
        mesh.setLineWidth(PATH_LINE_WIDTH);
        
        //makes the spatial object out of the mesh
        Spatial probePathLine = new Geometry("Line", mesh);
        probePathLine.setName("probeLine");
        probePathLine.setLocalScale(1);
        probePathLine.setMaterial(material);
        return probePathLine;
    }
    
}
