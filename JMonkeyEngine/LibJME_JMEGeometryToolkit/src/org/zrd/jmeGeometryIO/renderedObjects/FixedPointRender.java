/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryIO.renderedObjects;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import org.zrd.geometryToolkit.pointTools.FixedPointIO;
import org.zrd.geometryToolkit.pointTools.PointData;

/**
 *
 * @author BLI
 */
public class FixedPointRender {

    public static Spatial initBox(Material ballMat, Vector3f position) {
        Box b = new Box(0.2F, 0.2F, 0.2F);
        Spatial sampleBox = new Geometry("FixedPoint", b);
        sampleBox.setMaterial(ballMat);
        sampleBox.setLocalTranslation(position);
        return sampleBox;
    }

    public static Node displayFixedPoints(FixedPointIO fixedPts, Material ptMaterial) {
        Node fixedPoints = new Node();
        for (PointData fixedPt : fixedPts.getFixedPointsOnMesh()) {
            fixedPoints.attachChild(initBox(ptMaterial, fixedPt.getPointCoords()));
        }
        return fixedPoints;
    }
    
}
