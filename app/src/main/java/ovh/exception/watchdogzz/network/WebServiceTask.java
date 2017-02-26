package ovh.exception.watchdogzz.network;

import android.content.Context;
import android.os.AsyncTask;
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

public class WebServiceTask extends AsyncTask<String, Void, JSONObject> {
    private IWSConsumer consumer;
    private Context context;
    private Boolean isDone;
    private User me;
    private JSONObject data = null;
    private int requestMethod = WebServiceTask.GET;

    public final static int GET = Request.Method.GET;
    public final static int POST = Request.Method.POST;

    public WebServiceTask(Context context, IWSConsumer consumer) {
        this.context = context;
        this.consumer = consumer;
        this.isDone = false;
        this.me = null;
        this.requestMethod = WebServiceTask.GET;
    }

    public WebServiceTask(Context context, IWSConsumer consumer, User me) {
        this.context = context;
        this.consumer = consumer;
        this.isDone = false;
        this.me = me;
        this.requestMethod = WebServiceTask.POST;
    }

    public final AsyncTask<String, Void, JSONObject> get(String url) {
        this.me = null;
        this.requestMethod = WebServiceTask.GET;
        return execute(url);
    }

    public final AsyncTask<String, Void, JSONObject> post(String url, User user) {
        this.me = user;
        this.requestMethod = WebServiceTask.POST;
        return execute(url);
    }

    protected JSONObject doInBackground(String... urls) {
        String url = urls[0];

        JSONObject result = null;
        JSONObject request = null;

        if(this.requestMethod==WebServiceTask.POST) {
            request = new JSONObject();
            JSONArray loc = new JSONArray();
            try {
                loc.put(me.getPosition().getLatitude());
                loc.put(me.getPosition().getLongitude());
                loc.put(me.getPosition().getAltitude());
                request.put("login", me.getName());
                request.put("name", me.getName());
                request.put("email", me.getEmail());
                request.put("photo", me.getPhotoUrl());
                request.put("token", me.getIdToken());
                request.put("location", loc);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("WS TASK", "Sent: " + request.toString());
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (requestMethod, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("WS TASK", "Received: " + response.toString());
                        data = response;
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
                Map<String, String> mHeaders = new HashMap<>();
                mHeaders.put("json", "True");
                mHeaders.put("rejectUnauthorized", "false");
                return mHeaders;
            }
        };

        // Access the RequestQueue through your singleton class.
        NetworkManager.getInstance(context).addToRequestQueue(jsObjRequest);

        while(!isDone);

        result = data;

        return result;
    }

    protected void onPostExecute(JSONObject result) {
        if(consumer!=null)
            consumer.consume(result);
    }
}