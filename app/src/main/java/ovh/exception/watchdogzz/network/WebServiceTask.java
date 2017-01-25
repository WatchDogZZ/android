package ovh.exception.watchdogzz.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class WebServiceTask extends AsyncTask<String, Void, JSONObject> {
    private IWSConsumer consumer;
    private Context context;
    private Boolean isDone;
    private JSONObject data = null;

    public WebServiceTask(Context context, IWSConsumer consumer) {
        this.context = context;
        this.consumer = consumer;
        this.isDone = false;
    }

    protected JSONObject doInBackground(String... urls) {
        String url = urls[0];

        JSONObject result = null;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("WS TASK", "Received: " + response.toString());
                        data = response;
                        isDone = true;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("WS TASK", "#" + error.getMessage());
                        isDone = true;
                    }
                });

        // Access the RequestQueue through your singleton class.
        NetworkManager.getInstance(context).addToRequestQueue(jsObjRequest);

        while(!isDone);

        result = data;

        return result;
    }

    protected void onPostExecute(JSONObject result) {
        consumer.consume(result);
    }
}