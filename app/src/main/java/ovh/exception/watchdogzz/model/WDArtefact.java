package ovh.exception.watchdogzz.model;

import android.content.Context;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by begarco on 19/11/2016.
 */

public class WDArtefact implements WDDrawable {
    private WDObjet objet;

    // Constructor - Setup the data-array buffers
    public WDArtefact(Context context) {
        objet = new WDObjet(WDObjet.Shape2D.CIRCLE, 0,0,0.125f,0.1250f,64);
    }

    // Render this shape
    public void draw(GL10 gl) {
        objet.draw(gl);
    }
}
