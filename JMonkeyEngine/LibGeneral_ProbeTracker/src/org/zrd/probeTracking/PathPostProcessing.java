/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking;

import java.nio.file.Path;
import org.zrd.geometryToolkit.geometryUtil.GeometryDataHelper;
import org.zrd.geometryToolkit.pathDataStructure.SegmentSet;
import org.zrd.geometryToolkit.pathTools.PathCompression;
import org.zrd.geometryToolkit.pathTools.PathHelper;
import org.zrd.util.dataHelp.DataArrayToStringConversion;
import org.zrd.util.dataHelp.OutputHelper;
import org.zrd.util.fileHelper.FileDataHelper;
import org.zrd.util.fileHelper.GeneralFileHelper;

/**
 *
 * @author BLI
 */
public class PathPostProcessing implements Runnable{
    
    private Path recordedPathStats;
    private Path compressedPathAndSignalFile;
    private Path compressedPathFile;
    
    private SegmentSet originalPathInformation;
    private float minSegmentLength;
    private DataArrayToStringConversion convertor;

    public PathPostProcessing(Path recordedPathStats, Path compressedPathAndSignalFile, 
            Path compressedPathFile, SegmentSet originalPathInformation, 
            float minSegmentLength, DataArrayToStringConversion convertor) {
        
        this.recordedPathStats = recordedPathStats;
        this.compressedPathAndSignalFile = compressedPathAndSignalFile;
        this.compressedPathFile = compressedPathFile;
        this.originalPathInformation = originalPathInformation;
        this.minSegmentLength = minSegmentLength;
        this.convertor = convertor;
    }

    @Override
    public void run() {
        System.out.println("Now beginning path post-processing. Please wait until it's over to end program.");
        
        //write the compressed path and signal info
        SegmentSet compressedFileAndSignalInfo = PathCompression.getCompressedPath(
                originalPathInformation, minSegmentLength);
        GeometryDataHelper.writeVerticesToFile(compressedFileAndSignalInfo.getPathVertices(), compressedPathFile);
        GeometryDataHelper.writeSegmentSetInfoToFile(compressedFileAndSignalInfo, convertor,compressedPathAndSignalFile);
        
        //write the path arc length
        FileDataHelper.exportLinesToFile(compressedFileAndSignalInfo.getResultStrings(), recordedPathStats);
        OutputHelper.printStringCollection(compressedFileAndSignalInfo.getResultStrings());
        
        System.out.println("PATH POST-PROCESSING OVER. IT IS SAFE TO TERMINATE THE PROGRAM");
        
    }
    
    
}
