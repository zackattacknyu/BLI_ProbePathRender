/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.bliProbePath;

/**
 *
 * This is all extra code
 *      from the main class
 *      that may be used in the future
 * 
 * 
 * @author Zach
 */
public class ExtraCodeMain {
    
    /*private void addBoxAtPoint(Vector3f point){
        Spatial currentBox = initBox(redLineMaterial);
        currentBox.setLocalTranslation(point);
        rootNode.attachChild(currentBox);
    }
    
    private Spatial initBox(Material boxMat){
        Box b = new Box(0.1f, 0.1f, 0.1f);
        Spatial sampleBox = new Geometry("Box", b);
        sampleBox.setName("locationBox");
        sampleBox.setLocalScale(1);
        sampleBox.setMaterial(boxMat);
        return sampleBox;
    }
    
    
    
    
    */
    
    
    /*
     * This displays each of the normals for all the triangles.
     * It is meant to be used to visually verify that all the normals
     *      point outside the surface
     */
    /*private void displayNormals(TriangleSet triangles){
        Node normals = new Node();
        Vector3f startPt;
        Vector3f endPt;
        float scaleFactor = 5.0f;
        Spatial currentNormal;
        ArrayList<Vector3f> currentNormalVerts;
        
        for(MeshTriangle tri: triangles.getTriangleList()){
            startPt = tri.getCenter();
            endPt = startPt.add(tri.getNormal().mult(scaleFactor));
            currentNormalVerts = new ArrayList<Vector3f>(2);
            currentNormalVerts.add(startPt); 
            currentNormalVerts.add(endPt);
            currentNormal = PathRenderHelper.createLineFromVertices(currentNormalVerts, lineMaterial);
            normals.attachChild(currentNormal);
        }
        
        rootNode.attachChild(normals);
    }*/
    
}
