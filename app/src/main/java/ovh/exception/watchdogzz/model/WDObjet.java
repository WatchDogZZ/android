package ovh.exception.watchdogzz.model;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created by begarco on 19/11/2016.
 */

public class WDObjet implements WDDrawable {
    private FloatBuffer vertexBuffer;  // Buffer for vertex-array
    private ByteBuffer indexBuffer;    // Buffer for index-array
    private int nbIndices;
    private Shape type;

    public enum Shape {
        CUSTOM,
        CIRCLE,
        RECTANGLE,
        TRIANGLE,
        SPHERE
    }

    public WDObjet(Shape s, float ... params) {

        // donnees de forme
        ArrayList<float[]> vertices = new ArrayList<>();
        ArrayList<int[]> faces = new ArrayList<>();

        switch (s) {
            case CUSTOM:
                break;
            case CIRCLE:    // x,y,z,rayon,precision
                if(params.length >= 2) {
                    // traitement des params
                    float[] center = {params[0],params[1],params.length>2?params[2]:0};
                    float rayon = params.length>3?params[3]:1;
                    int precision = params.length>4? (int) params[4] :64;

                    // precalculs
                    float pas = 2.0f * (float)Math.PI / (float)precision;
                    vertices.add(center);
                    for (int i = 0; i < precision + 1; i++){
                        float angle = i * pas;
                        float cx = (float) (rayon * cos(angle));
                        float cy = (float) (rayon * sin(angle));
                        float[] cur = { cx+center[0], cy+center[1], center[2] };

                        vertices.add(cur);
                    }
                    // ajout des faces
                    for(int i = 1 ; i <= precision ; ++i) {
                        int[] fac = { 0, i, i+1 };
                        faces.add(fac);
                    }
                    // generation
                    fromRawData(vertices, faces);
                } else {
                    Log.w(WDObjet.class.getCanonicalName(), "Need of x, y, z, rayon and precision to draw a circle.");
                }
                break;
            case RECTANGLE:
                break;
            case TRIANGLE:  // x1,y1,z1 , x2,y2,z2 , x3,y3,z3
                if(params.length != 9) {
                    Log.w(WDObjet.class.getCanonicalName(), "Need of x1,y1,z1 , x2,y2,z2 , x3,y3,z3 to draw a triangle.");
                } else {
                    for(int i = 0 ; i < 3 ; ++i) {
                        float[] a = {params[3*i], params[3*i+1], params[3*i+2]};
                        vertices.add(a);
                    }
                    int[] tri = { 0, 1, 2 };
                    faces.add(tri);
                    fromRawData(vertices, faces);
                }
                break;
            case SPHERE:
                if(params.length >= 2) {
                    // traitement des params
                    float[] center = {params[0], params[1], params.length > 2 ? params[2] : 0};
                    float rayon = params.length > 3 ? params[3] : 1;
                    float angleA, angleB;
                    float cos, sin;
                    float r1, r2;
                    float h1, h2;
                    float step = 30.0f;
                    float[] v = new float[3];
                    float ratio = (float)Math.PI / 180.0f;

                    for (angleA = -90.0f; angleA < 90.0f; angleA += step) {
                        int n = 0;

                        r1 = (float) Math.cos(angleA * ratio);
                        r2 = (float) Math.cos((angleA + step) * ratio);
                        h1 = (float) Math.sin(angleA * ratio);
                        h2 = (float) Math.sin((angleA + step) * ratio);

                        // Fixed latitude, 360 degrees rotation to traverse a weft
                        for (angleB = 0.0f; angleB <= 360.0f; angleB += step) {

                            cos = (float) Math.cos(angleB * ratio);
                            sin = -(float) Math.sin(angleB * ratio);

                            v[0] = (r2 * cos);
                            v[1] = (h2);
                            v[2] = (r2 * sin);
                            vertices.add(v);
                            v[0] = (r1 * cos);
                            v[1] = (h1);
                            v[2] = (r1 * sin);
                            vertices.add(v);

                            n += 2;
                        }
                    }

                    // tracer des faces
                    for (int i = 1; i < vertices.size()-1 ; ++i) {
                        faces.add(new int[]{i, i, i+1});
                    }

                    // generation
                    fromRawData(vertices, faces);
                }
                break;
            default:
                Log.w(WDObjet.class.getCanonicalName(), "You should use public WDObjet(ArrayList<float[]> vertices, ArrayList<int[]> faces)");
                break;
        }
        type = s;
    }

    public WDObjet(ArrayList<float[]> vertices, ArrayList<int[]> faces) {
        fromRawData(vertices, faces);
    }

    private void fromRawData(ArrayList<float[]> vertices, ArrayList<int[]> faces) {
        // Setup vertex-array buffer. Vertices in float. A float has 4 bytes.
        ByteBuffer vbb = ByteBuffer.allocateDirect((vertices.size() * Float.SIZE * 3) / Byte.SIZE);
        vbb.order(ByteOrder.nativeOrder()); // Use native byte order
        vertexBuffer = vbb.asFloatBuffer(); // Convert byte buffer to float
        for (float[] vtx : vertices) {
            vertexBuffer.put(vtx);
        }
        vertexBuffer.position(0);           // Rewind

        // Setup index-array buffer. Indices in byte.
        indexBuffer = ByteBuffer.allocateDirect((faces.size() * 3 * Integer.SIZE) / Byte.SIZE);
        for (int[] face : faces) {
            for (int val : face) {
                indexBuffer.put((byte) val);
            }
        }
        indexBuffer.position(0);

        nbIndices = faces.size() * 3;
    }

    // Render this shape
    public void draw(GL10 gl) {
        // Enable vertex-array and define the buffers
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

        // Draw the primitives via index-array
        gl.glDrawElements(GL10.GL_TRIANGLES, nbIndices, GL10.GL_UNSIGNED_BYTE, indexBuffer);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
