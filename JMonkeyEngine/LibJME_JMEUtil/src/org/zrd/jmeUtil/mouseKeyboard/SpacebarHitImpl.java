/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeUtil.mouseKeyboard;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;

/**
 *
 * @author BLI
 */
public class SpacebarHitImpl extends GeneralKeyboardActionMethod{
    
    private boolean spacebarHit = false;

    public SpacebarHitImpl(InputManager manager) {
        super(manager, "spacebarHit", KeyInput.KEY_SPACE);
    }

    @Override
    public void actionMethod() {
        System.out.println("spacebar hit");
        spacebarHit = true;
    }
    
    public void resetSpacebarHit(){
        spacebarHit = false;
    }

    public boolean wasSpacebarHit() {
        if(spacebarHit){
            spacebarHit = false;
            return true;
        }else{
            return false;
        }
    }
    
}
