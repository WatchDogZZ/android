package ovh.exception.watchdogzz.model;

import android.content.Context;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.microedition.khronos.opengles.GL10;

import ovh.exception.watchdogzz.data.User;

/**
 * Created by begarco on 19/11/2016.
 */

public class WDMap extends Observable implements WDDrawable, Observer {
    private WDObjet map;
    private HashMap<Integer,WDArtefact> markers;
    private Context context;

    // Constructor - Setup the data-array buffers
    public WDMap(Context context) {
        this.context = context;
        WDObjParser parser = new WDObjParser();
        parser.Parse(context, "isima2d_o");
        map = parser.GetObjects().get(0);
        markers = new HashMap<>();

        //markers.add(new Pair(new WDArtefact(context), new float[3]));
    }

    public void addMarker(int id, WDArtefact art) {
        markers.put(id,art);
    }

    // Render this shape
    public void draw(GL10 gl) {
        // dessin de la map
        map.draw(gl);

        // dessin des marqueurs
        for (WDArtefact x : markers.values()) {
            x.draw(gl);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof User) {
            User u = (User)data;
            WDArtefact ar;
            if(markers.containsKey(u.getId())) {    // faire update
                ar = markers.get(u.getId());
                ar.setPosition(u.getPosition());
                ar.setLabel(u.getName());
                ar.setInfo(u.getEmail());
            } else {    // faire ajout
                ar = new WDArtefact(context);
                ar.setPosition(u.getPosition());
                ar.setLabel(u.getName());
                ar.setInfo(u.getEmail());
                markers.put(u.getId(),ar);
            }
        } else if (data instanceof Integer) {
            int index = (int) data;
            if(markers.containsKey(index)) {
                markers.remove(index);
            }
        }
        this.hasChanged();
        this.notifyObservers();
    }
}
