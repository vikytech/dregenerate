package it.works.rehydrate;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.net.URI;
import java.util.Timer;

public class RequestTask extends AsyncTask<String, String, String> {

    public AfterAsyncTask delegate = null;
    private Timer myTimer;
    private MyTimerTask myTimerTask;

    @Override
    protected String doInBackground(String... uri) {
        HttpResponse response = null;
        HttpClient httpClient = new DefaultHttpClient();
        String responseString = null;
        int value = 0;
        try {
            response = httpClient.execute(new HttpGet(new URI(uri[0])));
            HttpEntity entity = response.getEntity();
            responseString = EntityUtils.toString(entity, "UTF-8");
            JSONObject jsonObject = new JSONObject(responseString);
            value = jsonObject.getInt("value");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(value);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
//        delegate.onComplete(values[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        delegate.onComplete(result);
        this.cancel(true);
    //@wip
//        myTimer = new Timer();
//        myTimerTask = new MyTimerTask();
//        myTimer.scheduleAtFixedRate(myTimerTask, 0, 6000);
    }
}