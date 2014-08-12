/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.bliProbePath;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapText;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;

/**
 *
 * @author BLI
 */
public class LiveTrackingText {
    
    private BitmapText yawPitchRollText, xyzText, recordingText, resetProbeText, probeMoveModeText;
    
    private Node guiNode;
    private BitmapFont guiFont;

    public void setYawPitchRollText(String yawPitchRollText) {
        this.yawPitchRollText.setText(yawPitchRollText);
    }

    public void setXyzText(String xyzText) {
        this.xyzText.setText(xyzText);
    }

    public void setRecordingText(String recordingText) {
        this.recordingText.setText(recordingText);
    }

    public void setResetProbeText(String resetProbeText) {
        this.resetProbeText.setText(resetProbeText);
    }

    public void setProbeMoveModeText(String probeMoveModeText) {
        this.probeMoveModeText.setText(probeMoveModeText);
    }
    

    
    public LiveTrackingText(Node guiNode, AssetManager assetManager){

        this.guiNode = guiNode;
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        
        float currentStartY = 0.0f;
        
        recordingText = initializeNewText(currentStartY);
        recordingText.setText("Press N to record a new path");
        currentStartY = currentStartY + recordingText.getLineHeight();
        
        resetProbeText = initializeNewText(currentStartY);
        resetProbeText.setText("Press H to reset probe to (0,0)");
        currentStartY = currentStartY + resetProbeText.getLineHeight();
        
        yawPitchRollText = initializeNewText(currentStartY);
        currentStartY = currentStartY + yawPitchRollText.getLineHeight();
        
        xyzText = initializeNewText(currentStartY);
        currentStartY = currentStartY + xyzText.getLineHeight();
        
        probeMoveModeText = initializeNewText(currentStartY);
    }
    
    private BitmapText initializeNewText(float currentStartY){
        
        BitmapText newText = new BitmapText(guiFont,false);
        newText.setColor(ColorRGBA.Red);
        newText.setSize(guiFont.getCharSet().getRenderedSize());
        newText.setLocalTranslation(0,currentStartY, 0);
        guiNode.attachChild(newText);
        return newText;
        
    }
    
}
