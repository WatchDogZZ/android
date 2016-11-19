package ovh.exception.watchdogzz.model;

import android.content.Context;
import android.util.Pair;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by begarco on 19/11/2016.
 */

public class WDMap implements WDDrawable {
    private WDObjet map;
    private ArrayList<Pair<WDArtefact,float[]>> markers;

    // Constructor - Setup the data-array buffers
    public WDMap(Context context) {
        WDObjParser parser = new WDObjParser();
        parser.Parse(context, "isima2d_o");
        map = parser.GetObjects().get(0);
        markers = new ArrayList<>();
        markers.add(new Pair(new WDArtefact(context), new float[3]));
    }

    public void addMarker(WDArtefact art, float[] position) {
        markers.add(new Pair<>(art, position));
    }

    // Render this shape
    public void draw(GL10 gl) {
        // dessin de la map
        map.draw(gl);

        // dessin des marqueurs
        for (Pair<WDArtefact,float[]> x : markers) {
            x.first.draw(gl);
        }
    }
}
