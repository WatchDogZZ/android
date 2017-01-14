package ovh.exception.watchdogzz.view;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by begarco on 19/11/2016.
 */

public class WDCamera {
    private float[] mPosition;

    /**
     * Defini la position de la camera
     * @param x
     * @param y
     * @param z
     */
    public WDCamera(float x, float y, float z) {
        this.mPosition = new float[3];
        setPosition(x,y,z);
    }

    /**
     * Defini la position de la camera
     * @param x
     * @param y
     * @param z
     */
    public void setPosition(float x, float y, float z) {
        mPosition[0] = x;
        mPosition[1] = y;
        mPosition[2] = z;
    }

    /**
     * utilise la camera
     * @param gl
     */
    public void watch(GL10 gl) {
        gl.glTranslatef(x(),y(),z());
    }

    public float x() {
        return this.mPosition[0];
    }

    public float y() {
        return this.mPosition[1];
    }

    public float z() {
        return this.mPosition[2];
    }
}
