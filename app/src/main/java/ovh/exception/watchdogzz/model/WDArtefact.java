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
    private float r,g,b;

    // Constructor - Setup the data-array buffers
    public WDArtefact(Context context) {
        this(context,"","");
    }
    public WDArtefact(Context context, String label, String info) {
        this.objet = new WDObjet(WDObjet.Shape.CIRCLE, 0,0,0,0.05f,64);
        this.setLabel(label);
        this.setInfo(info);
    }

    // Render this shape
    public void draw(GL10 gl) {
        gl.glColor4f(r,g,b,1.0f);
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
        int hash = label.hashCode();
        r = (hash % 10)/10f;
        hash/=10;
        g = (hash % 10)/10f;
        hash/=10;
        b = (hash % 10)/10f;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
