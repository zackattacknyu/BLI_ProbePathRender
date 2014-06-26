/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.utilImpl.mouseKeyboard;

import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;

/**
 * This is simplify a class that is the JMonkeyEngine implementation
 *      of whether the mouse is currently being held down or not.
 * 
 * It is meant to be used whenever click and drag is enabled to 
 *      tell other actions that the mouse is being pressed currently
 *
 * @author BLI
 */
public class MouseClickImpl extends GeneralMouseActionMethod{

    /**
     * Tells whether the mouse is currently being pressed down
     */
    private boolean mouseDown = false;
    
    /**
     * constructor to instantiate the listener for the mouse
     * @param manager   the input manager currently being used
     */
    public MouseClickImpl(InputManager manager){
        super(manager,"cameraMouseClick",MouseInput.BUTTON_LEFT);
    }
    
    /**
     * This is called when the mouse is pressed down or up
     *      and simplify switches the boolean variable telling that
     */
    @Override
    public void actionMethod() {
        mouseDown = !mouseDown;
    }

    /**
     * tells whether the mouse is pressed down currently
     * @return      whether or not mouse is down
     */
    public boolean isMouseDown() {
        return mouseDown;
    }
    
    
    
}
