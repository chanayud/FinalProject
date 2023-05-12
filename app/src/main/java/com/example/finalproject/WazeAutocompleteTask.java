package com.example.finalproject;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class WazeAutocompleteTask extends AsyncTask<String, Void, List<AddressSuggestion>> {
    private WazeAutoCompleteListener listener;

    private static final String TAG = "WazeAutocompleteTask";
    private static final String API_URL = "https://search-api.waze.com/autocomplete";
//    private static String API_URL = "https://search.waze.com";
//    private static final String APP_ID = "YOUR_APP_ID";
//    private static final String APP_CODE = "YOUR_APP_CODE";
    private static final String CHARSET = "UTF-8";
    private static final String LATITUDE = "37.422408";
    private static final String LONGITUDE = "-122.085609";
    private String API_KEY = "AIzaSyBm0Cpi9QK-vH6ou2HYtWGw0WkA92KhDJ0";



    public WazeAutocompleteTask(WazeAutoCompleteListener listener){
        this.listener = listener;
    }

    @Override
    protected List<AddressSuggestion> doInBackground(String... params) {
        String searchPrefix = params[0];
        String query = null;
        try {
            query = API_URL + "?api_key=" + API_KEY + "&q=" + URLEncoder.encode(searchPrefix, CHARSET) + "&lat=" + LATITUDE + "&lon=" + LONGITUDE;

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        List<AddressSuggestion> results = new ArrayList<>();
        try {
            URL url = new URL(query);
            Log.d("TAG", query);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept-Charset", CHARSET);
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String response = reader.readLine();
                JSONObject json = new JSONObject(response);
                JSONArray predictions = json.getJSONArray("predictions");
                for (int i = 0; i < predictions.length(); i++) {
                    JSONObject prediction = predictions.getJSONObject(i);
                    String displayName = prediction.getString("displayName");
                    double lat = prediction.getDouble("latitude");
                    double lon = prediction.getDouble("longitude");
                    results.add(new AddressSuggestion(displayName, lat, lon));
                }
            } else {
                Log.e(TAG, "HTTP error: " + connection.getResponseCode() + " " + connection.getResponseMessage());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage(), e);
        }
        return results;
    }

    @Override
    protected void onPostExecute(List<AddressSuggestion> results) {
        super.onPostExecute(results);
        listener.onWazeAutoCompleteResult(results);
    }
}

