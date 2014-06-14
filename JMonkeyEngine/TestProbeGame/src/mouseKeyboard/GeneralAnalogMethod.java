/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mouseKeyboard;

import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;

/**
 *
 * @author BLI
 */
public abstract class GeneralAnalogMethod {

    public GeneralAnalogMethod(InputManager manager, String name, int mouseAxisCode,boolean negative){
        manager.addMapping(name, new MouseAxisTrigger(mouseAxisCode,negative));
        
        AnalogListener anl = new AnalogListener() {

            public void onAnalog(String name, float value, float tpf) {
                analogMethod();
            } 
        };
        
        manager.addListener(anl, name);
        
    }
    
    public abstract void analogMethod();
    
}
