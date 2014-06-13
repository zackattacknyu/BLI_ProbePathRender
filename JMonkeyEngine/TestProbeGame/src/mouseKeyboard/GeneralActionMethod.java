/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mouseKeyboard;

import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

/**
 *
 * @author BLI
 */
public abstract class GeneralActionMethod {

    public GeneralActionMethod(InputManager manager, String name, int keyCode){
        manager.addMapping(name, new KeyTrigger(keyCode));
        
        ActionListener acl = new ActionListener() {

            public void onAction(String name, boolean keyPressed, float tpf) {
                    actionMethod();
            } 
        };
        
        manager.addListener(acl, name);
        
    }
    
    public abstract void actionMethod();
    
}
