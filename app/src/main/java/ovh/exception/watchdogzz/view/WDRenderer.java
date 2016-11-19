package ovh.exception.watchdogzz.view;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ovh.exception.watchdogzz.model.WDMap;


/**
 * Created by begarco on 19/11/2016.
 */

public class WDRenderer implements Renderer {

    private Context context;    // contexte de l'application
    private WDCamera camera;    // camera
    private WDMap map;          // modele de la carte

    // Constructeur avec contexte
    public WDRenderer(Context context) {
        this.context = context;
        this.setMap(new WDMap(context));
        this.camera = new WDCamera(0, 0, -8.0f);
    }

    /**
     * Methode appelee lors de la premiere creation
     * @param gl
     * @param config
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);  // Set color's clear-value to black
        gl.glClearDepthf(1.0f);            // Set depth's clear-value to farthest
        gl.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
        gl.glDepthFunc(GL10.GL_LEQUAL);    // The type of depth testing to do
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  // nice perspective view
        gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
        gl.glDisable(GL10.GL_DITHER);      // Disable dithering for better performance

        /** possibilite de rajouter du code d'initialisation **/
    }

    /**
     * Methode de callback appelee lors d'une modification de la vue
     * @param gl
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (height == 0) height = 1;   // To prevent divide by zero
        float aspect = (float)width / height;

        // Set the viewport (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
        gl.glLoadIdentity();                 // Reset projection matrix
        // Use perspective projection
        GLU.gluPerspective(gl, 45, aspect, 0.1f, 100.f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);  // Select model-view matrix
        gl.glLoadIdentity();                 // Reset

        /** possibilite de rajouter du code pour le redimensionnement **/
    }

    /**
     * dessine la vue
     * @param gl
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        // nettoyage
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        /** dessin de la scene **/
        gl.glLoadIdentity();                 // Reset model-view matrix
        gl.glPushMatrix();
        this.camera.watch(gl);
        gl.glPushMatrix();
        this.getMap().draw(gl);                   // Draw model
        gl.glPopMatrix();
        gl.glPopMatrix();
    }

    public void moveCamera(float dx, float dy, float dz) {
        this.camera.setPosition(camera.x()+dx,camera.y()+dy,camera.z()+dz);
    }

    /**
     * effectue un zoom de la camera
     * @param coef
     */
    public void zoomCamera(float coef) {
        this.camera.setPosition(this.camera.x(), this.camera.y(), this.camera.z()*coef);
    }

    public WDMap getMap() {
        return map;
    }

    public void setMap(WDMap map) {
        this.map = map;
    }
}
