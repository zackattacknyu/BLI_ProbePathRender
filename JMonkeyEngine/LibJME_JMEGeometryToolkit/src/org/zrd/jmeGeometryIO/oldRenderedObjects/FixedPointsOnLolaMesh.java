/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryIO.oldRenderedObjects;

/**
 *
 * @author BLI
 *
public class FixedPointsOnLolaMesh {
    
    //TODO: make this come from properties file
    public static final Vector3f[] fixedPoints = {
        new Vector3f(-0.51208466f, -2.015924f, -14.905227f),
        new Vector3f(-4.103265f, -2.7216837f, -13.844656f),
        new Vector3f(-0.42267808f, -4.185834f, -17.14265f),
        new Vector3f(-4.0077333f, -4.704981f, -16.033361f)};
    public static final MeshTriangle[] correspondingTriangles = {
        new MeshTriangle(
            new Vector3f(-0.72895795f, -2.0336456f, -14.867519f),
            new Vector3f(-0.47583792f, -1.9271622f, -14.824425f),
            new Vector3f(-0.4999979f, -2.0336838f, -14.926361f)),
        new MeshTriangle(
            new Vector3f(-4.122718f, -2.8099117f, -13.903099f),
            new Vector3f(-4.2539973f, -2.7200909f, -13.752243f),
            new Vector3f(-4.0038376f, -2.677925f, -13.869957f)),
        new MeshTriangle(
            new Vector3f(-0.4999177f, -4.2599983f, -17.215199f),
            new Vector3f(-0.62903774f, -4.1757774f, -17.097366f),
            new Vector3f(-0.37511772f, -4.1455536f, -17.104164f)),
        new MeshTriangle(
            new Vector3f(-3.9985576f, -4.6944504f, -16.025059f),
            new Vector3f(-3.9950378f, -4.770878f, -16.18628f),
            new Vector3f(-4.2582374f, -4.843151f, -15.9549675f))};
    
    public static final PointData[] fixedPointData = {
        new PointOnMeshData(fixedPoints[0],correspondingTriangles[0]),
        new PointOnMeshData(fixedPoints[1],correspondingTriangles[1]),
        new PointOnMeshData(fixedPoints[2],correspondingTriangles[2]),
        new PointOnMeshData(fixedPoints[3],correspondingTriangles[3]),
    };
    
    public static final FixedPointPicker pointPicker = 
            new FixedPointPicker(Arrays.asList(fixedPointData));
    
}*/
