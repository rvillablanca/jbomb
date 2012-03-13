package jbomb.common.controls;

import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

public abstract class JBombAbstractControl extends AbstractControl implements Cloneable {

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        try {
            Control control = (Control) super.clone();
            spatial.addControl(control);
            return control;
        } catch (CloneNotSupportedException ex) {
            return newInstanceOfMe();
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    protected abstract Control newInstanceOfMe();
}
