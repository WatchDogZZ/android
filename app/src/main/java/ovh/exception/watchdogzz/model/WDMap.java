package ovh.exception.watchdogzz.model;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.microedition.khronos.opengles.GL10;

import ovh.exception.watchdogzz.data.User;

/**
 * Class for representing the map of an establishment
 */
public class WDMap extends Observable implements WDDrawable, Observer {
    private final float scale = 3169.8f;
    private WDObjet map;
    private HashMap<String,WDArtefact> markers;
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

    public void addMarker(String id, WDArtefact art) {
        markers.put(id,art);
    }

    // intersection of bat a and b
    private float [] origin = {45.759231f, 3.111185f, 0.0f};
    //float [] origin = {0,0,0};


    // Render this shape
    public void draw(GL10 gl) {
        // dessin de la map
        gl.glPushMatrix();
        gl.glPushMatrix();
        gl.glRotatef(180.0f,0.0f,1.0f,0.0f);
        gl.glRotatef(57.55046185f,0.0f,0.0f,1.0f);
        gl.glScalef(0.002f,0.002f,0.002f);
        gl.glTranslatef(-1682f,-1016f,0); // position dans le mesh du point d'origine
        gl.glColor4f(0,0,0,1.0f);
        map.draw(gl);
        gl.glPopMatrix();

        // dessin des marqueurs
        for (WDArtefact x : markers.values()) {
            gl.glPushMatrix();
            float[] tmp = x.getPosition().getForMap(getOrigin(), getScale());
            gl.glTranslatef(tmp[0],tmp[1], 0.5f);
            Log.d("ME", x.getPosition().toString() + " " + x.getLabel());

            x.draw(gl);
            gl.glPopMatrix();
        }
        gl.glPopMatrix();
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof User) {
            User u = (User)data;
            WDArtefact ar;
            if(markers.containsKey(u.getId())) {    // faire update
                Log.d("ARTEFACT", "update");
                ar = markers.get(u.getId());
                ar.setPosition(u.getPosition());
                ar.setLabel(u.getName());
                ar.setInfo(u.getEmail());
            } else {    // faire ajout
                Log.d("ARTEFACT", "add");
                ar = new WDArtefact(context);
                ar.setPosition(u.getPosition());
                ar.setLabel(u.getName());
                ar.setInfo(u.getEmail());
                markers.put(u.getId(),ar);
            }
        } else if (data instanceof String) {   // suppression
            String index = (String) data;
            Log.d("ARTEFACT", "remove");
            if(markers.containsKey(index)) {
                markers.remove(index);
            }
        }
        this.setChanged();
        this.notifyObservers();
    }

    public float[] getOrigin() {
        return origin;
    }

    public void setOrigin(float[] origin) {
        this.origin = origin;
    }

    public float getScale() {
        return scale;
    }
}
