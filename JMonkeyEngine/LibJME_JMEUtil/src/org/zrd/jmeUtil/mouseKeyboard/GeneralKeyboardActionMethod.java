/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeUtil.mouseKeyboard;

import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

/**
 * This is an abstract class whose implementing classes are meant to be
 *      actions carried out after a keyboard stroke
 * 
 * @author BLI
 */
public abstract class GeneralKeyboardActionMethod {

    /**
     * Initializes the class
     * @param manager       the application's input manager
     * @param name          the name of the action
     * @param keyCode       the key code for the keyboard key to trigger the action
     */
    public GeneralKeyboardActionMethod(InputManager manager, String name, int keyCode){
        
        //maps the name of the action to the keyboard input
        manager.addMapping(name, new KeyTrigger(keyCode));
        
        //makes the listener that will get executed every time an event occurs
        ActionListener acl = new ActionListener() {

            @Override
            public void onAction(String name, boolean keyPressed, float tpf) {
                if(keyPressed) actionMethod();
            } 
        };
        
        //maps the name of the action to the listener
        manager.addListener(acl, name);
        
    }
    
    /**
     * This is the method that will get called
     *      every time the keyboard key is hit
     */
    public abstract void actionMethod();
    
}
