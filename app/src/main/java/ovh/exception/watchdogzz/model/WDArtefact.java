package ovh.exception.watchdogzz.model;

import android.content.Context;

import javax.microedition.khronos.opengles.GL10;

import ovh.exception.watchdogzz.data.GPSPosition;

/**
 * Created by begarco on 19/11/2016.
 */

public class WDArtefact implements WDDrawable {
    private WDObjet objet;
    private GPSPosition position;
    private String label;
    private String info;

    // Constructor - Setup the data-array buffers
    public WDArtefact(Context context) {
        objet = new WDObjet(WDObjet.Shape.CIRCLE, 0,0,0,0.05f,64);
    }

    // Render this shape
    public void draw(GL10 gl) {
        objet.draw(gl);
    }

    public GPSPosition getPosition() {
        return position;
    }

    public void setPosition(GPSPosition position) {
        this.position = position;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
