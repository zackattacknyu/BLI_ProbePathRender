/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeUtil.mouseKeyboard;

import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;

/**
 * This is an abstract class whose implementing classes are meant to be
 *      actions carried out through a mouse click
 *
 * @author BLI
 */
public abstract class GeneralMouseActionMethod {

    /**
     * Initializes the class
     * @param manager       the application's input manager
     * @param name          the name of the action to put into the mapping
     * @param keyCode       the integer keycode of the mouse button
     */
    public GeneralMouseActionMethod(InputManager manager, String name, int keyCode){
        
        //maps the name of the action to the mouse trigger
        manager.addMapping(name, new MouseButtonTrigger(keyCode));
        
        //makes the listener method that acts when the key is pressed
        ActionListener acl = new ActionListener() {

            @Override
            public void onAction(String name, boolean keyPressed, float tpf) {
                actionMethod();
            } 
        };
        
        //maps the listener to the mapping name made above
        manager.addListener(acl, name);
        
    }
    
    /**
     * This is the method that will get called every time
     *  that mouse button is pressed down
     */
    public abstract void actionMethod();
    
}
