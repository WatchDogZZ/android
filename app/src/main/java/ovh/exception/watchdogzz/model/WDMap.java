package ovh.exception.watchdogzz.model;

import android.content.Context;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by begarco on 19/11/2016.
 */

public class WDMap {
    private WDObjet map;

    // Constructor - Setup the data-array buffers
    public WDMap(Context context) {
        WDObjParser parser = new WDObjParser();
        parser.Parse(context, "isima2d_o");
        map = parser.GetObjects().get(0);
    }

    // Render this shape
    public void draw(GL10 gl) {
        map.draw(gl);
    }
}
