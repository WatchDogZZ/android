package ovh.exception.watchdogzz.view;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

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


    private final float[] mat_ambient = { 0.2f, 0.3f, 0.8f, 1.0f };
    private FloatBuffer mat_ambient_buf;
    // Parallel incident light
    private final float[] mat_diffuse = { 0.4f, 0.6f, 0.9f, 1.0f };
    private FloatBuffer mat_diffuse_buf;
    // The highlighted area
    private final float[] mat_specular = { 0.2f * 0.4f, 0.2f * 0.6f, 0.2f * 0.8f, 1.0f };
    private FloatBuffer mat_specular_buf;

    public volatile float mLightX = 0f;
    public volatile float mLightY = 0f;
    public volatile float mLightZ = 3f;


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
        gl.glClearColor(0.63f, 0.63f, 0.63f, 1.0f);  // Set color's clear-value to black
        gl.glClearDepthf(1.0f);            // Set depth's clear-value to farthest
        gl.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
        gl.glDepthFunc(GL10.GL_LEQUAL);    // The type of depth testing to do
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  // nice perspective view
        gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
        gl.glDisable(GL10.GL_DITHER);      // Disable dithering for better performance

        /** possibilite de rajouter du code d'initialisation **/

        initBuffers();
    }

    private void initBuffers() {
        ByteBuffer bufTemp = ByteBuffer.allocateDirect(mat_ambient.length * 4);
        bufTemp.order(ByteOrder.nativeOrder());
        mat_ambient_buf = bufTemp.asFloatBuffer();
        mat_ambient_buf.put(mat_ambient);
        mat_ambient_buf.position(0);

        bufTemp = ByteBuffer.allocateDirect(mat_diffuse.length * 4);
        bufTemp.order(ByteOrder.nativeOrder());
        mat_diffuse_buf = bufTemp.asFloatBuffer();
        mat_diffuse_buf.put(mat_diffuse);
        mat_diffuse_buf.position(0);

        bufTemp = ByteBuffer.allocateDirect(mat_specular.length * 4);
        bufTemp.order(ByteOrder.nativeOrder());
        mat_specular_buf = bufTemp.asFloatBuffer();
        mat_specular_buf.put(mat_specular);
        mat_specular_buf.position(0);
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

        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);

        // Texture of material
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, mat_ambient_buf);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, mat_diffuse_buf);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, mat_specular_buf);
        // Specular exponent 0~128 less rough
        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 96.0f);

        //The position of the light source
        float[] light_position = {mLightX, mLightY, mLightZ, 0.0f};
        ByteBuffer mpbb = ByteBuffer.allocateDirect(light_position.length*4);
        mpbb.order(ByteOrder.nativeOrder());
        FloatBuffer mat_posiBuf = mpbb.asFloatBuffer();
        mat_posiBuf.put(light_position);
        mat_posiBuf.position(0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, mat_posiBuf);

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
