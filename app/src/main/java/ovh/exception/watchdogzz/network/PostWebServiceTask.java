package ovh.exception.watchdogzz.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.ArrayMap;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ovh.exception.watchdogzz.data.User;

public class PostWebServiceTask extends AsyncTask<String, Void, JSONObject> {
    private IWSConsumer consumer;
    private Context context;
    private Boolean isDone;
    private User me;

    public PostWebServiceTask(Context context, IWSConsumer consumer, User me) {
        this.context = context;
        this.consumer = consumer;
        this.isDone = false;
        this.me = me;
    }

    protected JSONObject doInBackground(String... urls) {
        String url = urls[0];

        JSONObject request = new JSONObject();
        JSONArray loc = new JSONArray();
        try {
            loc.put(me.getPosition().getLatitude());
            loc.put(me.getPosition().getLongitude());
            loc.put(me.getPosition().getAltitude());
            request.put("login", me.getName());
            request.put("name", me.getName());
            request.put("location", loc);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Map<String, String> mHeaders = new HashMap<>();
        mHeaders.put("json", "True");

        Log.d("WS TASK", "Sent: " + request.toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("WS TASK", "Received: " + response.toString());
                        isDone = true;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("WS TASK", "#" + error.getMessage());
                        isDone = true;
                    }
                }) {
            public Map<String, String> getHeaders() {
                return mHeaders;
            }
        };

        // Access the RequestQueue through your singleton class.
        NetworkManager.getInstance(context).addToRequestQueue(jsObjRequest);

        while(!isDone);

        return request;
    }

    protected void onPostExecute(JSONObject result) {

    }
}