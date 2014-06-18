/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meshTraversal;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.mesh.IndexBuffer;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 *
 * @author BLI
 */
public class MeshData {
    
    private ArrayList<Vector2f> textureCoordinates;
    private Mesh mesh;
    
    public MeshData(Mesh mesh){
        this.mesh = mesh;
        constructTextureCoordList();
        
    }

    public ArrayList<Vector2f> getTextureCoordinates() {
        return textureCoordinates;
    }
    
    private FloatBuffer obtainMeshBuffer(VertexBuffer.Type type){
        VertexBuffer pb = mesh.getBuffer(type);
        if(pb == null) return null;
        FloatBuffer fpb = (FloatBuffer) pb.getData();
        return fpb;
    }
    
    private void constructTextureCoordList(){
        FloatBuffer fb = obtainMeshBuffer(VertexBuffer.Type.TexCoord);
        if(fb == null){
            textureCoordinates = null;
            return;
        }
        textureCoordinates = new ArrayList<Vector2f>(mesh.getVertexCount());
        fb.clear();
        while(fb.remaining() > 0){
            float x = fb.get();
            float y = fb.get();
            textureCoordinates.add(new Vector2f(x,y));
        }
    }
    
    public TriangleTexture getTriangleTextCoords(int index){
        
        if(textureCoordinates == null) return null;
        
        IndexBuffer ib = mesh.getIndicesAsList();

        // aquire triangle's vertex indices
        int vertIndex = index * 3;
        int vert1 = ib.get(vertIndex);
        int vert2 = ib.get(vertIndex+1);
        int vert3 = ib.get(vertIndex+2);
        
        return new TriangleTexture(textureCoordinates.get(vert1),
                textureCoordinates.get(vert2),
                textureCoordinates.get(vert3));
    }
    
}
