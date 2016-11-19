package ovh.exception.watchdogzz.model;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by begarco on 19/11/2016.
 */

public class WDObjet {
    private FloatBuffer vertexBuffer;  // Buffer for vertex-array
    private ByteBuffer indexBuffer;    // Buffer for index-array
    private int nbIndices;

    public WDObjet(ArrayList<float[]> vertices, ArrayList<int[]> faces) {
        // Setup vertex-array buffer. Vertices in float. A float has 4 bytes.
        ByteBuffer vbb = ByteBuffer.allocateDirect((vertices.size() * Float.SIZE * 3) / Byte.SIZE);
        vbb.order(ByteOrder.nativeOrder()); // Use native byte order
        vertexBuffer = vbb.asFloatBuffer(); // Convert byte buffer to float
        for (float[] vtx : vertices) {
            vertexBuffer.put(vtx);
            Log.d("V", String.valueOf(vtx[0]) + " " + String.valueOf(vtx[1]) + " " + String.valueOf(vtx[2]));
        }
        vertexBuffer.position(0);           // Rewind

        // Setup index-array buffer. Indices in byte.
        indexBuffer = ByteBuffer.allocateDirect((faces.size()*3*Integer.SIZE) / Byte.SIZE);
        for (int[] face : faces) {
            for( int val : face) {
                indexBuffer.put((byte) val);
            }
            Log.d("FACE", String.valueOf(face[0])+String.valueOf(face[1])+String.valueOf(face[2]));
        }
        indexBuffer.position(0);

        nbIndices = faces.size()*3;
    }

    // Constructor pour le triangle
   /* public WDObjet() {
        // Setup vertex-array buffer. Vertices in float. A float has 4 bytes.
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder()); // Use native byte order
        vertexBuffer = vbb.asFloatBuffer(); // Convert byte buffer to float
        vertexBuffer.put(vertices);         // Copy data into buffer
        vertexBuffer.position(0);           // Rewind

        // Setup index-array buffer. Indices in byte.
        indexBuffer = ByteBuffer.allocateDirect(indices.length);
        indexBuffer.put(indices);
        indexBuffer.position(0);
    }
*/
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
