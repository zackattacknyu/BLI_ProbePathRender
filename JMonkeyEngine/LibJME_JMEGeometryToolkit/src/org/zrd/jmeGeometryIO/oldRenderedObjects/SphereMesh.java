/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryIO.oldRenderedObjects;

/**UNUSED CODE BUT KEEP FOR POTENTIAL FUTURE USES
 * 
 * This gets the mesh that is a sphere that was used
 *      for debugging purposes
 *
 * @author Zach
 *
public class SphereMesh extends MeshRenderData{

    public SphereMesh(AssetManager assetManager){
        
        String sphereLocation = "Models/sphere2.obj";
        //String sphereLocation = "Models/simpleCube.obj";
        
        Material sphereMaterial = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        sphereMaterial.setTexture("ColorMap",assetManager.loadTexture("Textures/lola_texture.png"));
        //if(wireframeOn) sphereMaterial.getAdditionalRenderState().setWireframe(true);
        
        renderedMesh = MeshInputHelper.generateModel(sphereLocation, sphereMaterial, assetManager);

        float surfaceScale = 20f;
        Matrix4f sphereTransform = new Matrix4f();
        sphereTransform.setScale(20f, 20f, 20f);

        renderedMesh.scale(surfaceScale);
        
        finalMeshInfo = new TriangleSet();
        finalMeshInfo.setTransform(sphereTransform);
        finalMeshInfo = MeshInputHelper.addToTriangleSet(finalMeshInfo,renderedMesh,sphereTransform);
    }
    
    
    
}*/
