/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probeTrackingRender;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;
import org.zrd.probeTracking.ProbeTracker;

/**
 *
 * @author BLI
 */
public class ResetTracker extends GeneralKeyboardActionMethod{
    
    private ProbeTracker probeTracker;
    
    public ResetTracker(InputManager inputManager, ProbeTracker probeTracker){
        super(inputManager,"resetTracker",KeyInput.KEY_H);
        this.probeTracker = probeTracker;
    }

    @Override
    public void actionMethod() {
        probeTracker.resetProbeReader();
        probeTracker.resetProbe();
        
    }
    
}
