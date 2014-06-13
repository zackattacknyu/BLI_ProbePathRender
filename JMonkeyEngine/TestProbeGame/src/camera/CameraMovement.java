/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package camera;

import com.jme3.input.InputManager;
import mouseKeyboard.GeneralActionMethod;

/**
 *
 * @author BLI
 */
public class CameraMovement extends GeneralActionMethod{
    
    private String displayText;
    
    public CameraMovement(InputManager manager, String name, int keyCode, String displayText){
        super(manager,name,keyCode);
        this.displayText = displayText;
    }

    @Override
    public void actionMethod() {
        System.out.println(displayText);
    }
    
}
