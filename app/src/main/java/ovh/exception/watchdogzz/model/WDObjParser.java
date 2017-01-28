package ovh.exception.watchdogzz.model;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by begarco on 19/11/2016.
 */

public class WDObjParser {

    private ArrayList<float[]> vertices;
    private ArrayList<float[]> vt;
    private ArrayList<float[]> vn;
    private ArrayList<int[]> faces;
    private ArrayList<int[]> textures;
    private ArrayList<int[]> normales;
    private ArrayList<WDObjet> objets;
    private boolean isParsed;
    private boolean isCreatingObject;

    public WDObjParser() {
        Clear();
    }

    /**
     * Parse un fichier obj en objets 3d
     * @param context
     * @param filename
     */
    public void Parse(Context context, String filename) {

        Clear();

        try {
            InputStream inputStream = context.getResources().openRawResource(
                    context.getResources().getIdentifier(filename,
                            "raw", context.getPackageName()));

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {

                    if(receiveString.contains("v ")) {	// lorsque l'on trouve un sommet
                        float[] line = parseFloat(receiveString.substring(2).split(" "),3);
                        for(int f = 0 ; f < line.length ; ++f)
                            line[f] = line[f];
                        vertices.add(line);
                    } else if(receiveString.contains("vt ")) {	// lorsque l'on trouve un sommet de texture
                        try {
                            float[] line = parseFloat(receiveString.substring(3).split(" "),2);
                            vt.add(line);
                        } catch (Exception e) {
                            Log.d("VT",receiveString+" "+receiveString.substring(3).split(" "));
                        }
                    } else if(receiveString.contains("vn ")) {	// lorsque l'on trouve une normale
                        try {
                            float[] line = parseFloat(receiveString.substring(3).split(" "),3);
                            vn.add(line);
                        } catch (Exception e) {
                            Log.d("VN",receiveString+" "+receiveString.substring(3).split(" "));
                        }
                    } else if(receiveString.contains("mtllib ")) {	// chargement des textures
                        /// TODO peut etre si on fait une texture
                    } else if(receiveString.contains("f ")) {		// lorsque l'on trouve une face
                        String[] tmp = receiveString.substring(2).split(" ");

                        int [] c = new int[tmp.length];	// indices des vertices
                        int [] d = new int[tmp.length];	// indices des textures
                        int [] e = new int[tmp.length];	// indices des normales

                        for (int j = 0; j < c.length; j++) {
                            c[j] = Integer.parseInt(tmp[j].split("/")[0])-1;
                            d[j] = Integer.parseInt(tmp[j].split("/")[1])-1;
                            e[j] = Integer.parseInt(tmp[j].split("/")[2])-1;
                        }

                        AppendTriangularized(c,faces);
                        AppendTriangularized(d,textures);
                        AppendTriangularized(e,normales);
                    } else if(receiveString.contains("o ")) {		// lorsque l'on trouve un nouvel objet
                        if(this.isCreatingObject)
                            CreateWDObjet();
                        this.isCreatingObject = true;
                    } else if(receiveString.contains("g ")) {		// lorsque l'on trouve un nouveau groupe
                        /// pas utile
                    } else if(receiveString.contains("usemtl ")) {		// lorsque l'on trouve un nouveau materiau
                        /// TODO peut etre si on fait une texture
                    }
                }

                inputStream.close();

                // convertir en objet
                if(isCreatingObject)
                    CreateWDObjet();

                this.isCreatingObject = false;

                isParsed = true;
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

    }

    /**
     * Nettoie le parser
     */
    public void Clear() {
        vertices = new ArrayList<>();
        vt = new ArrayList<>();
        vn = new ArrayList<>();
        faces = new ArrayList<>();
        textures = new ArrayList<>();
        normales = new ArrayList<>();
        objets = new ArrayList<>();
        isParsed = false;
        isCreatingObject = false;
    }

    private void CreateWDObjet() {
        WDObjet res = new WDObjet(vertices, faces);
        faces = new ArrayList<>();
        objets.add(res);
    }

    /**
     * Convertit un tableau de string en tableau de float
     * @param str
     * @return
     */
    private float[] parseFloat(String[] str, int size) {
        float[] result = new float[size];
        for (int i = 0; i < size; ++i) {
            result[i] = Float.parseFloat(str[i]);
        }
        return result;
    }

    /**
     * Convertit un tableau de string en tableau de int
     * @param str
     * @return
     */
    private int[] parseInt(String[] str) {
        int[] result = new int[str.length];
        for (int i = 0; i < str.length; ++i) {
            result[i] = Integer.parseInt(str[i]);
        }
        return result;
    }

    /**
     * Ajoute une face sous forme de triangles
     * @param face
     * @param array
     */
    private void AppendTriangularized(int[] face, ArrayList<int[]> array) {
        if(face.length == 3) {
            array.add(face);
        } else if(face.length == 4) {
            int[] faceA = new int[3];
            int[] faceB = new int[3];

            for(int i = 0; i<3; ++i) {  // creation des triangles
                faceA[i] = face[i];
                faceB[i] = face[(i + 2)%4];
            }

            // ajout des traingles
            array.add(faceA);
            array.add(faceB);
        }
    }

    ArrayList<WDObjet> GetObjects() {
        return isParsed ? objets : null;
    }
}
