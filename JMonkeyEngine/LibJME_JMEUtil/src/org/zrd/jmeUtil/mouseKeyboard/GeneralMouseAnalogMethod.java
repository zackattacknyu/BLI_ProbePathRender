/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeUtil.mouseKeyboard;

import com.jme3.input.InputManager;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;

/**
 * This is an abstract class whose implementing classes are meant to be
 *      actions carried out when a mouse is being held down
 *
 * @author BLI
 */
public abstract class GeneralMouseAnalogMethod {
    
    /**
     * Initializes the class
     * @param manager           the application's input manager
     * @param name              the name of the action to put into the mapping                  
     * @param mouseAxisCode     integer keycode for the mouse axis (x or y)
     * @param negative          whether or not we care about positive or negative movement
     */
    public GeneralMouseAnalogMethod(InputManager manager, String name, int mouseAxisCode,boolean negative){
        
        //maps the name to the mouse axis trigger
        manager.addMapping(name, new MouseAxisTrigger(mouseAxisCode,negative));
        
        //makes the analog listener that will execute when the trigger is pulled
        AnalogListener anl = new AnalogListener() {

            @Override
            public void onAnalog(String name, float value, float tpf) {
                analogMethod();
            } 
        };
        
        //maps the analog listener to the above mapping
        manager.addListener(anl, name);
        
    }
    
    /**
     * This method will get called every time the mouse is dragged
     */
    public abstract void analogMethod();
    
}
