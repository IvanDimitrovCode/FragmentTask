package com.example.ivandimitrov.multithreading;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.example.ivandimitrov.multithreading.NodeTypes.CitiesWeather;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ivan Dimitrov on 12/12/2016.
 */

public class CallAPI extends AsyncTask<CitiesWeather, CitiesWeather, CitiesWeather> {
    String result;
    CustomListener listener;

    public CallAPI(CustomListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected CitiesWeather doInBackground(CitiesWeather... params) {
        CitiesWeather cities[] = params;
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            sendPost(cities[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cities[0];
    }

    private CitiesWeather sendPost(CitiesWeather city) throws Exception {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city.getName() + "&APPID=8f992c317929ab6e9923c20ca01a5fd0");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            readStream(in);
        } finally {
            urlConnection.disconnect();
        }
        return parseMessage(city);
    }

    private void readStream(InputStream in) {
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder total = new StringBuilder();
        String line;
        try {
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
        } catch (IOException e) {
        }
        result = total.toString();
//        Log.v("RESULT", result);
    }

    private CitiesWeather parseMessage(CitiesWeather newCity) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject coordinates = jsonObj.getJSONObject("coord");
            JSONObject mainData = jsonObj.getJSONObject("main");
            JSONObject windData = jsonObj.getJSONObject("wind");

            newCity.setName(jsonObj.getString("name"));
            newCity.setLon(coordinates.getString("lon"));
            newCity.setLat(coordinates.getString("lat"));

            newCity.setWindSpeed(windData.getString("speed"));
            newCity.setTemperature(mainData.getString("temp"));
//            publishProgress();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newCity;
    }

    @Override
    protected void onProgressUpdate(CitiesWeather... values) {
        //CitiesWeather cities[] = values;
        //listener.onElementReceived(cities[0]);
    }


    @Override
    protected void onPostExecute(CitiesWeather result) {
        listener.onElementReceived(result);
    }

    public interface CustomListener {
        public void onDataReceived(ArrayList<CitiesWeather> siteNodes);

        public void onElementReceived(CitiesWeather city);
    }
}
