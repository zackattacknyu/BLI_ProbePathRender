/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package camera;

import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import mouseKeyboard.GeneralMouseActionMethod;

/**
 *
 * @author BLI
 */
public class CameraMouseClick extends GeneralMouseActionMethod{

    private boolean mouseDown = false;
    
    public CameraMouseClick(InputManager manager){
        super(manager,"cameraMouseClick",MouseInput.BUTTON_LEFT);
    }
    
    @Override
    public void actionMethod() {
        mouseDown = !mouseDown;
    }

    public boolean isMouseDown() {
        return mouseDown;
    }
    
    
    
}
